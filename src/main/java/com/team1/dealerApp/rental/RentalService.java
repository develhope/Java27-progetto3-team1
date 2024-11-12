package com.team1.dealerApp.rental;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.team1.dealerApp.paypal.PayPalService;
import com.team1.dealerApp.subscription.Subscription;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.utils.Pager;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.movie.MovieService;
import com.team1.dealerApp.video.tvshow.TvShow;
import com.team1.dealerApp.video.tvshow.TvShowService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserService userService;
    private final PayPalService payPalService;
    private final MovieService movieService;
    private final TvShowService tvShowService;

    private final Pager pager;

	public PurchaseConfirmDTO addRental( UserDetails user, CreateRentalDTO createRentalDTO ) throws BadRequestException, PayPalRESTException {
		validateRentalRequest(createRentalDTO);

		List < Movie > movies = fetchMovies(createRentalDTO);
		List < TvShow > tvShows = fetchTvShows(createRentalDTO);
		User renter = userService.getUserByEmail(user);
		double totalPrice = calculateTotalRentalPrice(movies, tvShows, renter.getSubscriptions());

		Rental rental = createRental(user, createRentalDTO, movies, tvShows, totalPrice);
		updateMovieProfit(movies, renter.getSubscriptions());
		updateShowProfit(tvShows, renter.getSubscriptions());

		Rental rental1 = rentalRepository.save(rental);
		String url = "http://localhost:8080/api/paypal/success/rental?orderId=" + rental1.getId();

		if(totalPrice > 0){
			Payment payment = payPalService.createPayment(totalPrice, "EUR", "paypal", "sale", "New rental", url);
			rental.setRentalStatus(RentalStatus.SUSPENDED);
			String urlMessage = payment.getLinks().stream()
					.filter(link -> "approval_url".equals(link.getRel()))
					.findFirst()
					.map(Links::getHref)
					.orElse(null);

			return generateOutput(urlMessage);
		}

		rental.setRentalStatus(RentalStatus.ACTIVE);
		return generateOutput("Ordine inserito");
	}

	public PurchaseConfirmDTO generateOutput(String message){
		return PurchaseConfirmDTO.builder()
				.message(message)
				.build();
	}

    private void updateMovieProfit(List<Movie> movies, List<Subscription> subscriptions) {
        subscriptions.forEach(subscription -> {
            if ("MOVIE".equals(subscription.getSubscriptionType().toString())) {
                movies.forEach(movie -> movie.setOrderCount(movie.getOrderCount() + 1));
            } else {
                movies.forEach(movie -> {
                    movie.setVideoProfit(movie.getVideoProfit() + movie.getRentalPrice());
                    movie.setOrderCount(movie.getOrderCount() + 1);
                });
            }
        });
    }

    private void updateShowProfit(List<TvShow> shows, List<Subscription> subscriptions) {
        subscriptions.forEach(subscription -> {
            if ("TV_SHOW".equals(subscription.getSubscriptionType().toString())) {
                shows.forEach(show -> show.setOrderCount(show.getOrderCount() + 1));
            } else {
                shows.forEach(show -> {
                    show.setVideoProfit(show.getVideoProfit() + show.getRentalPrice());
                    show.setOrderCount(show.getOrderCount() + 1);
                });
            }
        });
    }

    private void validateRentalRequest(CreateRentalDTO createRentalDTO) throws BadRequestException {
        if (createRentalDTO.getMovies().isEmpty() && createRentalDTO.getTvShows().isEmpty()) {
            throw new BadRequestException("Both lists cannot be empty");
        }
    }

    private List<Movie> fetchMovies(CreateRentalDTO createRentalDTO) {
        return createRentalDTO.getMovies().isEmpty() ? List.of() : createRentalDTO.getMovies().stream().map(movieService::getMovieById).toList();
    }

    private List<TvShow> fetchTvShows(CreateRentalDTO createRentalDTO) {
        return createRentalDTO.getTvShows().isEmpty() ? List.of() : createRentalDTO.getTvShows().stream().map(tvShowService::getShowById).toList();
    }

	private double calculateTotalRentalPrice( List < Movie > movies, List < TvShow > tvShows, List<Subscription> subscriptions ) {
		double movieRentalPrice = movies.stream().mapToDouble(Movie::getRentalPrice).sum();
		double tvShowRentalPrice = tvShows.stream().mapToDouble(TvShow::getRentalPrice).sum();
		if ( subscriptions.stream().anyMatch(s-> "MOVIE".equals(s.getSubscriptionType().toString()))) movieRentalPrice = 0;
		if ( subscriptions.stream().anyMatch(s->"TV_SHOW".equals(s.getSubscriptionType().toString()))) tvShowRentalPrice = 0;
		return movieRentalPrice + tvShowRentalPrice;
	}

    private Rental createRental(UserDetails user, CreateRentalDTO createRentalDTO, List<Movie> movieList, List<TvShow> tvShowsList, double totalPrice) {
        Rental rental = rentalMapper.toRental(createRentalDTO, movieList, tvShowsList);
        rental.setRentalPrice(totalPrice);
        rental.setStartDate(LocalDate.now());
        rental.setEndDate(LocalDate.now().plusDays(14));
        rental.setRenter(userService.getUserByEmail(user));
        rental.setRentalStatus(RentalStatus.ACTIVE);
        return rental;
    }

    public Page<RentalDTO> getActiveUserRentals(UserDetails user, int page, int size) throws NoSuchElementException {
        Page<Rental> rentalsFind = rentalRepository.findByRenter_Email(user.getUsername(), pager.createPageable(page, size));
        if (rentalsFind == null || rentalsFind.isEmpty()) {
            throw new NoSuchElementException("Rental's list is empty");
        }
        return rentalsFind.map(rentalMapper::toDTO);
    }

    public RentalDTO updateRentalEndDate(UserDetails user, Long id, LocalDate date) throws BadRequestException {

        Rental rentalFound = rentalRepository.findByRenter_EmailAndId(user.getUsername(), id).orElseThrow(() -> new NoSuchElementException("There is no rental with id " + id));
        LocalDate rentalEndDate = rentalFound.getEndDate();

        if (rentalEndDate.isAfter(date)) {
            throw new BadRequestException("Error: " + date + " is before the rental's end date");
        }
        rentalFound.setEndDate(date);
        rentalRepository.save(rentalFound);

        return rentalMapper.toDTO(rentalFound);
    }

    public boolean deleteRental(Long id) {
        rentalRepository.deleteById(id);
        return true;
    }

    public RentalDTO updateRentalStatus(Long orderId, String rentalStatus) {
        Rental rental = rentalRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException("There is no rental with id " + orderId));
        rental.setRentalStatus(RentalStatus.valueOf(rentalStatus.toUpperCase()));
        rentalRepository.save(rental);
        return rentalMapper.toDTO(rental);
    }
}
