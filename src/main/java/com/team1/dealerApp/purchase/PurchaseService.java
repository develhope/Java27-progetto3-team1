package com.team1.dealerApp.purchase;

import com.team1.dealerApp.subscription.Subscription;
import com.team1.dealerApp.user.Role;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.movie.MovieService;
import com.team1.dealerApp.video.tvshow.TvShow;
import com.team1.dealerApp.video.tvshow.TvShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final UserService userService;
    private final MovieService movieService;
    private final TvShowService tvShowService;

    public PurchaseDTO addPurchase(UserDetails user, CreatePurchaseDTO createPurchaseDTO) throws BadRequestException {
        validatePurchaseRequest(createPurchaseDTO);

        List<Movie> movieList = fetchMovies(createPurchaseDTO);
        List<TvShow> tvShowList = fetchTvShows(createPurchaseDTO);

        User purchaser = userService.getUserByEmail(user);

        double totalPurchasePrice = calculateTotalPrice(movieList, tvShowList, purchaser.getSubscriptions());

		Purchase purchase = createPurchase(createPurchaseDTO, movieList, tvShowList, totalPurchasePrice, purchaser);
		updateMovieProfit(movieList, purchaser.getSubscriptions());
		updateShowProfit(tvShowList, purchaser.getSubscriptions());
		purchaseRepository.save(purchase);

        return purchaseMapper.toDTO(purchase);
    }


	private void updateMovieProfit(List<Movie> movies, List<Subscription> subscriptions){
        subscriptions.forEach(subscription -> {
            if ("MOVIE".equals(subscription.getSubscriptionType().toString())) {
                movies.forEach(movie -> movie.setOrderCount(movie.getOrderCount() + 1));
            } else {
                movies.forEach(movie -> {
                    movie.setVideoProfit(movie.getVideoProfit() + movie.getPurchasePrice());
                    movie.setOrderCount(movie.getOrderCount() + 1);
                });
            }
        });
	}

	private void updateShowProfit(List<TvShow> shows, List<Subscription> subscriptions){
        subscriptions.forEach(subscription -> {
            if ("TV_SHOW".equals(subscription.getSubscriptionType().toString())) {
                shows.forEach(show -> show.setOrderCount(show.getOrderCount() + 1));
            } else {
                shows.forEach(show-> {
                    show.setVideoProfit(show.getVideoProfit() + show.getPurchasePrice());
                    show.setOrderCount(show.getOrderCount() + 1);
                });
            }
        });
	}

	private void validatePurchaseRequest( CreatePurchaseDTO createPurchaseDTO ) throws BadRequestException {
		if ( createPurchaseDTO.getMovies().isEmpty() && createPurchaseDTO.getTvShows().isEmpty() ) {
			throw new BadRequestException("Both lists cannot be empty");
		}
	}

    private List<Movie> fetchMovies(CreatePurchaseDTO createPurchaseDTO) {
        return createPurchaseDTO.getMovies().isEmpty() ? List.of() : createPurchaseDTO.getMovies().stream().map(movieService::getMovieById).toList();
    }

    private List<TvShow> fetchTvShows(CreatePurchaseDTO createPurchaseDTO) {
        return createPurchaseDTO.getTvShows().isEmpty() ? List.of() : createPurchaseDTO.getTvShows().stream().map(tvShowService::getShowById).toList();
    }

    private double calculateTotalPrice(List<Movie> movies, List<TvShow> tvShows, List<Subscription> subscriptions) {
        double movieTotal = movies.stream().mapToDouble(Movie::getPurchasePrice).sum();
        double tvShowTotal = tvShows.stream().mapToDouble(TvShow::getPurchasePrice).sum();
        if ( subscriptions.stream().anyMatch(s-> "MOVIE".equals(s.getSubscriptionType().toString()))) movieTotal -= movieTotal * 0.4;
        if ( subscriptions.stream().anyMatch(s->"TV_SHOW".equals(s.getSubscriptionType().toString()))) tvShowTotal -= tvShowTotal * 0.4;
        return movieTotal + tvShowTotal;
    }

    private Purchase createPurchase(CreatePurchaseDTO createPurchaseDTO, List<Movie> movies, List<TvShow> tvShows, double totalPrice, User purchaser) {

        Purchase purchase = purchaseMapper.toPurchase(createPurchaseDTO, movies, tvShows);
        purchase.setOrderStatus(OrderStatus.PAID);
        purchase.setPurchasePrice(totalPrice);
        purchase.setPurchaser(purchaser);
        return purchase;
    }


    public Page<PurchaseDTO> getPurchaseByUserId(UserDetails user, int page, int size) throws NoSuchElementException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Purchase> purchaseFind = purchaseRepository.findByPurchaser_Email(user.getUsername(), pageable);

        if (purchaseFind.isEmpty()) {
            throw new NoSuchElementException("User with email " + user.getUsername() + " has no purchases");
        }
        return purchaseFind.map(purchaseMapper::toDTO);
    }


    public PurchaseDTO updatePurchase(Long id, CreatePurchaseDTO createPurchaseDTO) throws NoSuchElementException {
        purchaseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Purchase whit " + id + " not found!"));
        List<Movie> purchasedMovies = movieService.getAllMoviesById(createPurchaseDTO.getMovies());
        List<TvShow> purchasedTvShow = tvShowService.getAllTvShowsById(createPurchaseDTO.getTvShows());
        Purchase purchaseSelected = purchaseMapper.toPurchase(createPurchaseDTO, purchasedMovies, purchasedTvShow);
        purchaseSelected.setId(id);
        purchaseRepository.save(purchaseSelected);

        return purchaseMapper.toDTO(purchaseSelected);
    }


    public boolean deletePurchaseById(Long id) {
        purchaseRepository.deleteById(id);
        return true;
    }
}
