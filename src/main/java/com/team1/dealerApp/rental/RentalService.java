package com.team1.dealerApp.rental;

import com.team1.dealerApp.user.Role;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RentalService {
	private final RentalRepository rentalRepository;
	private final RentalMapper rentalMapper;
	private final UserService userService;

	public RentalDTO addRental( UserDetails user, CreateRentalDTO createRentalDTO ) throws BadRequestException {
		validateRentalRequest(createRentalDTO);

		List < Movie > movies = fetchMovies(createRentalDTO);
		List < TvShow > tvShows = fetchTvShows(createRentalDTO);
		User renter = userService.getUserByEmail(user);
		double totalPrice = calculateTotalRentalPrice(movies, tvShows, renter.getRole());

		Rental rental = createRental(user, createRentalDTO, totalPrice);
		rentalRepository.save(rental);

		return rentalMapper.toDTO(rental);
	}

	private void validateRentalRequest( CreateRentalDTO createRentalDTO ) throws BadRequestException {
		if ( createRentalDTO.getMovies().isEmpty() && createRentalDTO.getTvShows().isEmpty() ) {
			throw new BadRequestException("Both lists cannot be empty");
		}
	}

	private List < Movie > fetchMovies( CreateRentalDTO createRentalDTO ) {
		return createRentalDTO.getMovies().isEmpty()? List.of(): createRentalDTO.getMovies();
	}

	private List < TvShow > fetchTvShows( CreateRentalDTO createRentalDTO ) {
		return createRentalDTO.getTvShows().isEmpty()? List.of(): createRentalDTO.getTvShows();
	}

	private double calculateTotalRentalPrice( List < Movie > movies, List < TvShow > tvShows, Role role ) {
		double movieRentalPrice = movies.stream().mapToDouble(Movie::getRentalPrice).sum();
		double tvShowRentalPrice = tvShows.stream().mapToDouble(TvShow::getRentalPrice).sum();
		if ( role.equals(Role.ROLE_TVSHOWS) ) tvShowRentalPrice = 0;
		if ( role.equals(Role.ROLE_MOVIES) ) movieRentalPrice = 0;
		return movieRentalPrice + tvShowRentalPrice;
	}

	private Rental createRental( UserDetails user, CreateRentalDTO createRentalDTO, double totalPrice ) {
		Rental rental = rentalMapper.toRental(createRentalDTO);
		rental.setRentalPrice(totalPrice);
		rental.setStartDate(LocalDateTime.now());
		rental.setEndDate(LocalDateTime.now().plusDays(14));
		rental.setRenter(userService.getUserByEmail(user));
		rental.setRentalStatus(RentalStatus.ACTIVE);
		return rental;
	}

	public Page < RentalDTO > getActiveUserRentals( UserDetails user, int page, int size ) throws NoSuchElementException {
		Pageable pageable = PageRequest.of(page, size);
		Page < Rental > rentalsFind = rentalRepository.findByRenter_Email(user.getUsername(), pageable);
		if ( rentalsFind.isEmpty() ) {
			throw new NoSuchElementException("Rental's list is empty");
		}
		return rentalsFind.map(rentalMapper::toDTO);
	}

	public RentalDTO updateRentalEndDate( UserDetails user, Long id, LocalDateTime dateTime ) {
		Rental rentalFound = rentalRepository.findByRenter_EmailAndId(user.getUsername(), id).orElseThrow(() -> new NoSuchElementException("There is no rental with id " + id));
		rentalFound.setEndDate(dateTime);
		rentalRepository.save(rentalFound);
		return rentalMapper.toDTO(rentalFound);
	}

	public boolean deleteRental( Long id ) {
		rentalRepository.deleteById(id);
		return true;
	}

}
