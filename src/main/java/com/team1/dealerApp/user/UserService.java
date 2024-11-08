package com.team1.dealerApp.user;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.team1.dealerApp.paypal.PayPalService;
import com.team1.dealerApp.subscription.*;
import com.team1.dealerApp.utils.Pager;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.movie.MovieMapper;
import com.team1.dealerApp.video.movie.MovieService;
import com.team1.dealerApp.video.tvshow.TvShow;
import com.team1.dealerApp.video.tvshow.TvShowMapper;
import com.team1.dealerApp.video.tvshow.TvShowService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings( "unused" )
public class UserService {
	private static final String USER_EMAIL_ERROR = "There is no user with email ";

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final SubscriptionService subscriptionService;
	private final SubscriptionMapper subscriptionMapper;
	private final PayPalService payPalService;
	private final Pager pager;
	private final MovieService movieService;
	private final TvShowService tvShowService;
	private final MovieMapper movieMapper;
	private final TvShowMapper tvShowMapper;


	public UserDTO getUserDTOById( UUID id ) throws NoSuchElementException {
		User getUser = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with Id " + id + " not found"));
		return userMapper.toUserDTO(getUser);
	}

	public UserDTO updateUser( UserDetails user, CreateUserDTO createUserDTO ) throws NoSuchElementException {
		Optional < User > userFound = userRepository.findByEmail(user.getUsername());

		if ( userFound.isEmpty() ) {
			throw new NoSuchElementException("This User doesn't exist");
		}

		User updateUser = userMapper.toUser(createUserDTO);
		updateUser.setId(userFound.get().getId());
		userRepository.save(updateUser);

		return userMapper.toUserDTO(updateUser);
	}

	public boolean deleteUser( UserDetails user ) {
		boolean isActive = false;
		User userToDelete = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException("There is no user with email " + user.getUsername()));
		userToDelete.setActive(isActive);
		userToDelete.getSubscriptions().forEach(s -> s.setStatus(isActive));
		userRepository.save(userToDelete);
		return true;
	}

	public boolean deleteUser( UUID id ) {
		boolean isActive = false;
		User userToDelete = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("There is no user with id " + id));
		userToDelete.setActive(isActive);
		userToDelete.getSubscriptions().forEach(s -> s.setStatus(isActive));
		userRepository.save(userToDelete);
		return true;
	}


	public UserDTO registerUser( CreateUserDTO userDTO ) throws BadRequestException {
		User user = userMapper.toUser(userDTO);
		if ( userRepository.existsByEmail(user.getEmail()) ) {
			throw new BadRequestException("Email already exists");
		}
		// Crittografia della password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return userMapper.toUserDTO(user);
	}

	public User getUserById( UUID id ) throws NoSuchElementException {
		return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No User with Id " + id));

	}

	public Page < UserDTO > getAllUser( int page, int size ) {
		Page < User > allUser = userRepository.findAll(pager.createPageable(page, size));
		return allUser.map(userMapper::toUserDTO);
	}

	public UserDTO getUserDetails( UserDetails user ) {
		User userFound = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
		return userMapper.toUserDTO(userFound);
	}

	public User getUserByEmail( UserDetails user ) {
		return userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
	}

	public String updateSubscriptionPlan( UserDetails user, String subscription ) throws NoSuchElementException, PayPalRESTException {
		String email = user.getUsername();
		User updatable = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + email));

		SubscriptionType subscriptionType = SubscriptionType.valueOf(subscription.toUpperCase());
		updatable.getSubscriptions().removeIf(s -> s.getSubscriptionType().equals(subscriptionType));
		Subscription newSubscription = new Subscription();
		newSubscription.setSubscriptionType(subscriptionType);
		newSubscription.setUsers(updatable);
		newSubscription.setPrice(20.00);
		newSubscription.setStartDate(LocalDate.now());
		newSubscription.setEndDate(LocalDate.now().plusDays(30));

		SubscriptionDTO subscription1 = subscriptionService.addSubscription(newSubscription);
		updatable.getSubscriptions().add(newSubscription);
		String url = "http://localhost:8080/api/paypal/success/purchase?orderId=" + subscription1.getId();
		Payment payment = payPalService.createPayment(newSubscription.getPrice(), "EUR", "paypal", "sale", "Subscription", url);

		userRepository.save(updatable);

		return payment.getLinks().stream().filter(link -> "approval_url".equals(link.getRel())).findFirst().map(Links::getHref).orElse(null);
	}

	public UserDTO updateSubscriptionEndDate( UserDetails user, Long subscriptionId, LocalDate date ) throws NoSuchElementException {
		subscriptionService.updateSubscriptionEndDate(subscriptionId, date);
		User userFound = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
		return userMapper.toUserDTO(userFound);
	}

	public UserDTO deleteSubscription( UserDetails user, Long subscriptionId ) {
		subscriptionService.getSubscriptionDetails(subscriptionId);
		User userFound = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
		return userMapper.toUserDTO(userFound);
	}

	public WatchedVideosDTO updateWatchedVideos( UserDetails user, UpdateWatchedVideoList videoList ) throws BadRequestException {

		if ( videoList.getMovieList().isEmpty() && videoList.getShowList().isEmpty() ) {
			throw new BadRequestException("Both list cannot be empty");
		}

		User loggedIn = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException("No user with email " + user.getUsername()));

		List < Movie > watchedMovies = getMovies(loggedIn, videoList.getMovieList());
		loggedIn.setWatchedMovies(watchedMovies);

		List < TvShow > watchedShows = getTvShows(videoList, loggedIn);
		loggedIn.setWatchedShows(watchedShows);
		userRepository.save(loggedIn);

		return new WatchedVideosDTO(watchedMovies.stream().map(movieMapper::toMovieDTO).toList(), watchedShows.stream().map(tvShowMapper::toTvShowDTO).toList());
	}

	private List < TvShow > getTvShows( UpdateWatchedVideoList videoList, User loggedIn ) {
		List < TvShow > showToAdd = videoList.getShowList().stream().map(tvShowService::getShowById).toList();

		List < TvShow > watchedShows = loggedIn.getWatchedShows();
		watchedShows.addAll(showToAdd);

		return watchedShows;
	}

	public List < Movie > getMovies( User loggedIn, List < Long > movieList ) {
		List < Movie > movieToAdd = movieList.stream().map(movieService::getMovieById).toList();

		List < Movie > watchedMovies = loggedIn.getWatchedMovies();
		watchedMovies.addAll(movieToAdd);

		return watchedMovies;
	}

	public WatchedVideosDTO updateWishList( UserDetails user, UpdateWatchedVideoList videoList ) throws BadRequestException {

		if ( videoList.getMovieList().isEmpty() && videoList.getShowList().isEmpty() ) {
			throw new BadRequestException("Both list cannot be empty");
		}

		User loggedIn = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException("No user with email " + user.getUsername()));

		List < Movie > moviesWishList = getMovies(loggedIn, videoList.getMovieList());
		loggedIn.setMovieWishList(moviesWishList);

		List < TvShow > showWishList = getTvShows(videoList, loggedIn);
		loggedIn.setShowWishList(showWishList);
		userRepository.save(loggedIn);

		return new WatchedVideosDTO(moviesWishList.stream().map(movieMapper::toMovieDTO).toList(), showWishList.stream().map(tvShowMapper::toTvShowDTO).toList());
	}
}
