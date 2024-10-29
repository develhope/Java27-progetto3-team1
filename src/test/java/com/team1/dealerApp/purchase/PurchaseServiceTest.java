package com.team1.dealerApp.purchase;

import com.team1.dealerApp.rental.Rental;
import com.team1.dealerApp.rental.RentalStatus;
import com.team1.dealerApp.user.SubscriptionStatus;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.movie.MovieDTO;
import com.team1.dealerApp.video.movie.MovieService;
import com.team1.dealerApp.video.tvshow.TvShow;
import com.team1.dealerApp.video.tvshow.TvShowDTO;
import com.team1.dealerApp.video.tvshow.TvShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class PurchaseServiceTest {

    @InjectMocks
    private PurchaseService purchaseService;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private MovieService movieService;

    @Mock
    private TvShowService tvShowService;


    private Long purchaseId;
    private Purchase purchaseCompleteTest;
    private PurchaseDTO purchaseDTOCompleteTest;
    private CreatePurchaseDTO createPurchaseDTOCompleteTest;
    private UUID userId;
    private User userCompleteTest;
    private Rental rentalCompleteTest;


    //Entities - Purchase
    public Purchase defaultPurchase(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        Long purchaseId = 1L;

        //purchaseCompleteTest = new Purchase();

        purchaseCompleteTest.setId(purchaseId);
        purchaseCompleteTest.setOrderStatus(OrderStatus.AVAILABLE);
        purchaseCompleteTest.setPurchasePrice(19.99);
        purchaseCompleteTest.setMovies(defaultMovieList);
        purchaseCompleteTest.setTvShows(defaultTvShowList);
        purchaseCompleteTest.setPurchaser(defaultUser(defaultMovieList, defaultTvShowList));

        return purchaseCompleteTest;
    }

    public PurchaseDTO defaultPurchaseDTO(List<MovieDTO> defaultMovieDTOList, List<TvShowDTO> defaultTvShowDTOList) {

        //purchaseDTOCompleteTest = new PurchaseDTO();

        purchaseDTOCompleteTest.setId(purchaseId);
        purchaseDTOCompleteTest.setOrderStatus(OrderStatus.AVAILABLE);
        purchaseDTOCompleteTest.setPurchasePrice(19.99);
        purchaseDTOCompleteTest.setMovies(defaultMovieDTOList);
        purchaseDTOCompleteTest.setTvShows(defaultTvShowDTOList);
        purchaseDTOCompleteTest.setUserId(userId);

        return purchaseDTOCompleteTest;
    }

    public CreatePurchaseDTO defaultCreatePurchaseDTO(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        List<Long> movieIds = new ArrayList<>();
        List<Long> tvShowIds = new ArrayList<>();

        defaultMovieList.forEach(movie -> movieIds.add(movie.getId()));

        defaultTvShowList.forEach(tvShow -> tvShowIds.add(tvShow.getId()));

        //createPurchaseDTOCompleteTest = new CreatePurchaseDTO();
        createPurchaseDTOCompleteTest.setMovies(movieIds);
        createPurchaseDTOCompleteTest.setTvShows(tvShowIds);

        return createPurchaseDTOCompleteTest;
    }


    //Entity - User
    public User defaultUser(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        //userCompleteTest = new User();

        userCompleteTest.setId(userId);
        userCompleteTest.setFirstName("Mario");
        userCompleteTest.setLastName("Rossi");
        userCompleteTest.setEmail("mario.rossi@gmail.com");
        userCompleteTest.setPhoneNumber("3331234567");
        userCompleteTest.setSubscriptionStatus(SubscriptionStatus.FULL_SUBSCRIPTION);
        userCompleteTest.setWatchedMovies(defaultMovieList);
        userCompleteTest.setWatchedShows(defaultTvShowList);

        List<Rental> rentalList = new ArrayList<>();

        rentalList.add(defaultRental(defaultMovieList, defaultTvShowList));
        userCompleteTest.setRentals(rentalList);

        return userCompleteTest;
    }


    //Entity - Rental
    public Rental defaultRental(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        Long rentalId = 1L;

        //rentalCompleteTest = new Rental();

        rentalCompleteTest.setId(rentalId);
        rentalCompleteTest.setStartDate(LocalDateTime.now());
        rentalCompleteTest.setEndDate(LocalDateTime.now().plusDays(14));
        rentalCompleteTest.setRentalStatus(RentalStatus.ACTIVE);
        rentalCompleteTest.setMovies(defaultMovieList);
        rentalCompleteTest.setTvShows(defaultTvShowList);
        rentalCompleteTest.setRentalPrice(20.00);

        return rentalCompleteTest;
    }


    // Lists - Movie/DTO
    public List<Movie> defaultMovieList() {

        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        Movie whatchedMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 110);
        Movie whatchedMovie2 = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 110);
        List<Movie> whatchedMovieList = new ArrayList<>();
        whatchedMovieList.add(whatchedMovie);
        whatchedMovieList.add(whatchedMovie2);

        return whatchedMovieList;
    }

    public List<MovieDTO> defaultMovieDTOList() {

        List<String> castMovieDTO = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2DTO = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        MovieDTO whatchedMovieDTO = new MovieDTO("Il signore degli anelli: il ritorno del Re", 110, Genre.FANTASY, castMovieDTO, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE);
        MovieDTO whatchedMovieDTO2 = new MovieDTO("Alice nel paese delle Meraviglie", 110, Genre.ANIMATION, castMovie2DTO, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE);
        List<MovieDTO> whatchedMovieDTOList = new ArrayList<>();
        whatchedMovieDTOList.add(whatchedMovieDTO);
        whatchedMovieDTOList.add(whatchedMovieDTO2);

        return whatchedMovieDTOList;
    }

    // Lists - TvShow/DTO
    public List<TvShow> defaultTvShowList() {

        List<String> castShow = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List<String> castShow2 = Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");
        TvShow whatchedTvShow = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 6, 52);
        TvShow whatchedTvShow2 = new TvShow("The Boys", Genre.ACTION, castShow2, "Erik Kripke", Year.of(2019), 60.0, 10.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 5, 40);
        List<TvShow> whatchedTvShowList = new ArrayList<>();
        whatchedTvShowList.add(whatchedTvShow);
        whatchedTvShowList.add(whatchedTvShow2);

        return whatchedTvShowList;
    }

    public List<TvShowDTO> defaultTvShowDTOList() {

        List<String> castShow = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List<String> castShow2 = Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");
        TvShowDTO whatchedTvShowDTO = new TvShowDTO("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, 6, 52, VideoStatus.RENTABLE);
        TvShowDTO whatchedTvShowDTO2 = new TvShowDTO("The Boys", Genre.ACTION, castShow2, "Erik Kripke", Year.of(2019), 60.0, 10.0, "Patriota impazzisce", 4.6f, 5, 40, VideoStatus.RENTABLE);
        List<TvShowDTO> whatchedTvShowDTOList = new ArrayList<>();
        whatchedTvShowDTOList.add(whatchedTvShowDTO);
        whatchedTvShowDTOList.add(whatchedTvShowDTO2);

        return whatchedTvShowDTOList;
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        purchaseId = 1L;
        purchaseCompleteTest = new Purchase();
        purchaseDTOCompleteTest = new PurchaseDTO();
        createPurchaseDTOCompleteTest = new CreatePurchaseDTO();
        userId = UUID.randomUUID();
        userCompleteTest = new User();
        rentalCompleteTest = new Rental();
    }


    //Tests
 /*
    //Test for addPurchase if-> User Created
    @Test
    void addPurchase_UserCreated() throws BadRequestException {

    }

    //Test for addPurchase if-> BadRequestException
    @Test
    void addPurchase_BadRequestException() {

    }


    //Test for getPurchaseByUserId if-> User Found
    @Test
    void getPurchaseByUserId_UserFound() {

    }

    //Test for getPurchaseByUserId if-> NoSuchElementException
    @Test
    void getPurchaseByUserId_NoSuchElementException() {

    }
 */


    //Test for updatePurchase if-> User Updated
    @Test
    void updatePurchase_UserUpdated() {

        Long purchaseId = 1L;
        List<Movie> movieList = defaultMovieList();
        List<TvShow> tvShowList = defaultTvShowList();
        CreatePurchaseDTO createPurchaseDTO = defaultCreatePurchaseDTO(movieList, tvShowList);

        Purchase existingPurchase = defaultPurchase(movieList, tvShowList);

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(existingPurchase));

        when(movieService.getAllMoviesById(createPurchaseDTO.getMovies())).thenReturn(movieList);
        when(tvShowService.getAllTvShowsById(createPurchaseDTO.getTvShows())).thenReturn(tvShowList);

        when(purchaseMapper.toPurchase(createPurchaseDTO, movieList, tvShowList)).thenReturn(existingPurchase);
        when(purchaseMapper.toDTO(existingPurchase)).thenReturn(defaultPurchaseDTO(defaultMovieDTOList(), defaultTvShowDTOList()));

        PurchaseDTO updatedPurchase = purchaseService.updatePurchase(purchaseId, createPurchaseDTO);

        assertNotNull(updatedPurchase);
        assertEquals(purchaseId, updatedPurchase.getId());

        verify(purchaseRepository, times(1)).save(existingPurchase);

    }


    //Test for updatePurchase if-> NoSuchElementException
    @Test
    void updatePurchase_NoSuchElementException() {

        Long purchaseId = 1L;
        CreatePurchaseDTO createPurchaseDTO = defaultCreatePurchaseDTO(defaultMovieList(), defaultTvShowList());

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                purchaseService.updatePurchase(purchaseId, createPurchaseDTO)
        );
        assertEquals("Purchase whit " + purchaseId + " not found!", exception.getMessage());
    }


    //Test for deletePurchaseById
    @Test
    void deletePurchaseById() {

        Long purchaseId = 1L;
        when(purchaseRepository.existsById(purchaseId)).thenReturn(true);

        boolean result = purchaseService.deletePurchaseById(purchaseId);

        verify(purchaseRepository, times(1)).deleteById(purchaseId);
        assertTrue(result);
    }

}
