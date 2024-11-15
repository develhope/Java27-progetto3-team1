package com.team1.dealerApp.rental;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.team1.dealerApp.paypal.PayPalService;
import com.team1.dealerApp.user.Role;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.utils.Pager;
import com.team1.dealerApp.video.AgeRating;
import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.movie.MovieDTO;
import com.team1.dealerApp.video.movie.MovieMapper;
import com.team1.dealerApp.video.movie.MovieService;
import com.team1.dealerApp.video.tvshow.TvShow;
import com.team1.dealerApp.video.tvshow.TvShowDTO;
import com.team1.dealerApp.video.tvshow.TvShowMapper;
import com.team1.dealerApp.video.tvshow.TvShowService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private UserService userService;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private TvShowMapper tvShowMapper;

    @Mock
    private MovieService movieService;

    @Mock
    private TvShowService tvShowService;

    @Mock
    private PayPalService payPalService;

    @Mock
    private Pager pager;

    @InjectMocks
    private RentalService rentalService;

    private Rental rental;
    private RentalDTO rentalDTO;
    private CreateRentalDTO createRentalDTO;
    private Long rentalId;
    private User userCompleteTest;
    private Page<Rental> rentalPage;


    // Lists - Movie
    public List<Movie> defaultMovieList() {

        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        Movie rentableMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.R, 110);
        Movie rentableMovie2 = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.G, 110);
        List<Movie> movieList = new ArrayList<>();
        movieList.add(rentableMovie);
        movieList.add(rentableMovie2);

        return movieList;
    }

    // Lists - TvShow
    public List<TvShow> defaultTvShowList() {

        List<String> castShow = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List<String> castShow2 = Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");
        TvShow rentableShow = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.NC17, 6, 52);
        TvShow rentableShow2 = new TvShow("The Boys", Genre.ACTION, castShow2, "Erik Kripke", Year.of(2019), 60.0, 10.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.PG, 5, 40);
        List<TvShow> tvShowList = new ArrayList<>();
        tvShowList.add(rentableShow);
        tvShowList.add(rentableShow2);

        return tvShowList;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        //Movie
        //From List<Movie> to List<Long>
        List<Long> movieIds = Arrays.asList(1L, 2L);

        //From List<Movie> to List<MovieDTO>
        List<MovieDTO> movieDTOList = defaultMovieList().stream()
                .map(movieMapper::toMovieDTO)
                .toList();


        //TvShow
        //From List<TvShow> to List<Long>
        List<Long> tvShowIds = Arrays.asList(1L, 2L);

        //From List<TvShow> to List<TvShowDTO>
        List<TvShowDTO> tvShowDTOList = defaultTvShowList().stream()
                .map(tvShowMapper::toTvShowDTO)
                .toList();


        rentalId = 1L;

        rental = new Rental();
        rental.setId(rentalId);
        rental.setStartDate(LocalDate.now());
        rental.setEndDate(LocalDate.now().plusDays(14));
        rental.setRentalStatus(RentalStatus.ACTIVE);
        rental.setMovies(defaultMovieList());
        rental.setTvShows(defaultTvShowList());
        rental.setRentalPrice(20.00);

        createRentalDTO = new CreateRentalDTO();
        createRentalDTO.setTvShows(tvShowIds);
        createRentalDTO.setMovies(movieIds);

        rentalDTO = new RentalDTO();
        rentalDTO.setId(rentalId);
        rentalDTO.setRentalPrice(40.00);
        rentalDTO.setRentalStatus(RentalStatus.ACTIVE);
        rentalDTO.setMovies(movieDTOList);
        rentalDTO.setTvShows(tvShowDTOList);
        rentalDTO.setStartDate(LocalDate.now());
        rentalDTO.setEndDate(LocalDate.now().plusDays(14));
    }

    public User defaultUser(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        userCompleteTest = new User();

        UUID userId = UUID.randomUUID();

        userCompleteTest.setId(userId);
        userCompleteTest.setFirstName("Mario");
        userCompleteTest.setLastName("Rossi");
        userCompleteTest.setEmail("mario.rossi@gmail.com");
        userCompleteTest.setPhoneNumber("3331234567");
        userCompleteTest.setWatchedMovies(defaultMovieList);
        userCompleteTest.setWatchedShows(defaultTvShowList);
        userCompleteTest.setRole(Role.ROLE_USER);

        List<Rental> rentalList = new ArrayList<>();

        rentalList.add(rental);
        userCompleteTest.setRentals(rentalList);

        return userCompleteTest;
    }

    @Test
    void testAddRental_ThrowsBadRequestException() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        createRentalDTO.setMovies(Collections.emptyList());
        createRentalDTO.setTvShows(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> rentalService.addRental(user, createRentalDTO));
    }

    @Test
    void testAddRental() throws BadRequestException, PayPalRESTException {
        User user = defaultUser(new ArrayList<>(), new ArrayList<>());
        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        Movie rentableMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.R, 110);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Payment payment = new Payment("Sales", payer);
        payment.setLinks(List.of(new Links("123abc", "456")));

        List<String> castShow = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        TvShow whatchedTvShow = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 6, 1000000.99, AgeRating.G, 5, 50);

        when(userService.getUserByEmail(user)).thenReturn(user);
        when(rentalRepository.save(rental)).thenReturn(rental);

        when(rentalMapper.toRental(any(CreateRentalDTO.class), anyList(), anyList())).thenReturn(rental);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);
        when(movieService.getMovieById(anyLong())).thenReturn(rentableMovie);
        when(tvShowService.getShowById(anyLong())).thenReturn(whatchedTvShow);

        when(payPalService.createPayment(anyDouble(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(payment);

        rentalService.addRental(user, createRentalDTO);

        assertEquals(40.0, rental.getRentalPrice());  // Assert the rental price is correct
    }

    @Test
    void testgetActiveUserRentals_ThrowsNoSuchElementException() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        rentalPage = new PageImpl<>(Collections.singletonList(null));
        Pageable pageable = PageRequest.of(0, 10);

        when(pager.createPageable(anyInt(), anyInt())).thenReturn(pageable);

        when(rentalRepository.findByRenter_Email(user.getUsername(), pageable)).thenReturn(Page.empty());

        assertThrows(NoSuchElementException.class, () -> rentalService.getActiveUserRentals(user, 0, 10));
    }

    @Test
    void getActiveUserRentals() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rental> rentalPage = new PageImpl<>(List.of(rental));

        when(pager.createPageable(anyInt(), anyInt())).thenReturn(pageable);

        when(rentalRepository.findByRenter_Email(user.getUsername(), pageable)).thenReturn(rentalPage);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        Page<RentalDTO> result = rentalService.getActiveUserRentals(user, 0, 10);

        assertEquals(1, result.getTotalElements());
    }


    @Test
    void testUpdateRentalEndDate() throws NoSuchElementException, BadRequestException {

        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        rentalPage = new PageImpl<>(Collections.singletonList(rental));

        LocalDate newEndDate = LocalDate.now().plusDays(30);
        when(rentalRepository.findByRenter_EmailAndId(eq(userCompleteTest.getUsername()), anyLong()))
                .thenReturn(Optional.of(rental));

        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        rentalService.updateRentalEndDate(user, rentalId, LocalDate.now().plusDays(30));

        assertEquals(newEndDate, rental.getEndDate());
    }

    @Test
    void testUpdateRentalEndDate_ThrowsNoSuchElementException() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rentalService.updateRentalEndDate(user, rentalId, LocalDate.now()));
    }


    @Test
    void testDeleteRental() {
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        rentalService.deleteRental(rentalId);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        Optional<Rental> deletedRental = rentalRepository.findById(rentalId);
        assertTrue(deletedRental.isEmpty());
    }

}
