package com.team1.dealerApp.purchase;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.team1.dealerApp.paypal.PayPalService;
import com.team1.dealerApp.rental.Rental;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

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

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private TvShowMapper tvShowMapper;

    @Mock
    UserService userService;

    @Mock
    private PayPalService payPalService;

    @Mock
    private Pager pager;


    private Long purchaseId;
    private Purchase purchaseCompleteTest;
    private PurchaseDTO purchaseDTOCompleteTest;
    private CreatePurchaseDTO createPurchaseDTOCompleteTest;
    private UUID userId;
    private User userCompleteTest;
    private Page<Purchase> purchasePage;


    //Entities - Purchase
    public Purchase defaultPurchase(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        Long purchaseId = 1L;

        purchaseCompleteTest.setId(purchaseId);
        purchaseCompleteTest.setOrderStatus(OrderStatus.AVAILABLE);
        purchaseCompleteTest.setPurchasePrice(19.99);
        purchaseCompleteTest.setMovies(defaultMovieList);
        purchaseCompleteTest.setTvShows(defaultTvShowList);
        purchaseCompleteTest.setPurchaser(defaultUser(defaultMovieList, defaultTvShowList));

        return purchaseCompleteTest;
    }

    public PurchaseDTO defaultPurchaseDTO(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        purchaseDTOCompleteTest.setId(purchaseId);
        purchaseDTOCompleteTest.setOrderStatus(OrderStatus.AVAILABLE);
        purchaseDTOCompleteTest.setPurchasePrice(19.99);

        UUID userId = UUID.randomUUID();

        List<MovieDTO> defaultMovieDTOList = defaultMovieList.stream()
                .map(movieMapper::toMovieDTO)
                .collect(Collectors.toList());
        List<TvShowDTO> defaultTvShowDTOList = defaultTvShowList().stream()
                .map(tvShowMapper::toTvShowDTO)
                .collect(Collectors.toList());

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

        createPurchaseDTOCompleteTest.setMovies(movieIds);
        createPurchaseDTOCompleteTest.setTvShows(tvShowIds);

        return createPurchaseDTOCompleteTest;
    }


    //Entity - User
    public User defaultUser(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        userCompleteTest.setId(userId);
        userCompleteTest.setFirstName("Mario");
        userCompleteTest.setLastName("Rossi");
        userCompleteTest.setEmail("mario.rossi@gmail.com");
        userCompleteTest.setPhoneNumber("3331234567");
        userCompleteTest.setRole(Role.ROLE_USER);
        userCompleteTest.setActive(true);
        userCompleteTest.setWatchedMovies(defaultMovieList);
        userCompleteTest.setWatchedShows(defaultTvShowList);

        List<Rental> rentalList = new ArrayList<>();

        userCompleteTest.setRentals(rentalList);

        return userCompleteTest;
    }


    // Lists - Movie/DTO
    public List<Movie> defaultMovieList() {

        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        Movie whatchedMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 110, 1000000.99, AgeRating.G, 120);
        Movie whatchedMovie2 = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 110, 800000.99, AgeRating.G, 120);
        List<Movie> whatchedMovieList = new ArrayList<>();
        whatchedMovieList.add(whatchedMovie);
        whatchedMovieList.add(whatchedMovie2);

        return whatchedMovieList;
    }

    public List<MovieDTO> defaultMovieDTOList() {

        return defaultMovieList().stream()
                .map(movieMapper::toMovieDTO)
                .collect(Collectors.toList());
    }

    // Lists - TvShow/DTO
    public List<TvShow> defaultTvShowList() {

        List<String> castShow = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List<String> castShow2 = Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");
        TvShow whatchedTvShow = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 6, 1000000.99, AgeRating.G, 5, 50);
        TvShow whatchedTvShow2 = new TvShow("The Boys", Genre.ACTION, castShow2, "Erik Kripke", Year.of(2019), 60.0, 10.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 5, 1000000.99, AgeRating.G, 5, 50);
        List<TvShow> whatchedTvShowList = new ArrayList<>();
        whatchedTvShowList.add(whatchedTvShow);
        whatchedTvShowList.add(whatchedTvShow2);

        return whatchedTvShowList;
    }

    public List<TvShowDTO> defaultTvShowDTOList() {

        return defaultTvShowList().stream()
                .map(tvShowMapper::toTvShowDTO)
                .collect(Collectors.toList());
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
        //rentalCompleteTest = new Rental();
    }


    //Tests
    //Test for addPurchase if-> User Created
    @Test
    void addPurchase_UserCreated() throws BadRequestException, PayPalRESTException {

        User user = defaultUser(new ArrayList<>(), new ArrayList<>());
        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        Movie purchasableMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.R, 110);
        purchasableMovie.setId(1L);
        TvShow purchasableTvShow = new TvShow("Breaking bad", Genre.DRAMA, castMovie, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 6, 1000000.99, AgeRating.G, 5, 50);
        purchasableTvShow.setId(1L);

        Purchase purchaseTest = defaultPurchase(defaultMovieList(), defaultTvShowList());

        Movie whatchedMovie1 = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 110, 1000000.99, AgeRating.G, 120);
        List<Movie> movieListTest = new ArrayList<>();
        movieListTest.add(whatchedMovie1);

        List<String> castShow = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        TvShow whatchedTvShow1 = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 6, 1000000.99, AgeRating.G, 5, 50);

        List<TvShow> tvShowListTest = new ArrayList<>();
        tvShowListTest.add(whatchedTvShow1);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Payment payment = new Payment("Sales", payer);
        payment.setLinks(List.of(new Links("123abc", "456")));

        when(userService.getUserByEmail(any(User.class))).thenReturn(user);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchaseTest);

        when(purchaseMapper.toPurchase(any(CreatePurchaseDTO.class),anyList(), anyList())).thenReturn(purchaseTest);
        when(movieService.getMovieById(anyLong())).thenReturn(purchasableMovie);
        when(tvShowService.getShowById(anyLong())).thenReturn(purchasableTvShow);

        when(payPalService.createPayment(anyDouble(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(payment);

        String message = purchaseService.addPurchase(user, new CreatePurchaseDTO(Collections.singletonList(1L), Collections.singletonList(1L)));

        assertEquals(80.0, purchaseCompleteTest.getPurchasePrice());
    }

    //Test for addPurchase if-> BadRequestException
    @Test
    void addPurchase_BadRequestException() {

        User user = defaultUser(new ArrayList<>(), new ArrayList<>());

        createPurchaseDTOCompleteTest.setMovies(Collections.emptyList());
        createPurchaseDTOCompleteTest.setTvShows(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> purchaseService.addPurchase(user, createPurchaseDTOCompleteTest));
    }


    //Test for getPurchaseByUserId if-> User Found
    @Test
    void getPurchaseByUserId_UserFound() {

        User user = defaultUser(new ArrayList<>(), new ArrayList<>());

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Purchase purchase = new Purchase();
        PurchaseDTO purchaseDTO = new PurchaseDTO();

        Page<Purchase> purchasePage = new PageImpl<>(List.of(purchase));

        when(pager.createPageable(page, size)).thenReturn(pageable);
        when(purchaseRepository.findByPurchaser_Email(user.getUsername(), pageable)).thenReturn(purchasePage);

        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        Page<PurchaseDTO> result = purchaseService.getPurchaseByUserId(user, page, size);

        assertEquals(1, result.getTotalElements());
    }


    @Test
    void testGetPurchaseByUserId_NoSuchElementException() {

        User user = defaultUser(new ArrayList<>(), new ArrayList<>());
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        when(pager.createPageable(page, size)).thenReturn(pageable);


        when(purchaseRepository.findByPurchaser_Email(user.getUsername(), pageable)).thenReturn(Page.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> purchaseService.getPurchaseByUserId(user, page, size)
        );

        assertEquals("User with email " + user.getUsername() + " has no purchases", exception.getMessage());
    }


    @Test
    void updatePurchase_UserUpdated() {

        List<Long> idList1 = Arrays.asList(1L,2L);
        List<Long> idList2 = Arrays.asList(1L,2L);

        Long purchaseId = 1L;
        Purchase existingPurchase = defaultPurchase(defaultMovieList(), defaultTvShowList());

        CreatePurchaseDTO createPurchaseDTO = new CreatePurchaseDTO(idList1, idList2);
        Purchase purchasedMapper = defaultPurchase(defaultMovieList(), defaultTvShowList());

        PurchaseDTO purchaseDTO = defaultPurchaseDTO(defaultMovieList(), defaultTvShowList());

        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(existingPurchase));

        when(movieService.getAllMoviesById(createPurchaseDTO.getMovies())).thenReturn(defaultMovieList());
        when(tvShowService.getAllTvShowsById(createPurchaseDTO.getTvShows())).thenReturn(defaultTvShowList());

        when(purchaseMapper.toPurchase(any(CreatePurchaseDTO.class), anyList(), anyList())).thenReturn(purchasedMapper);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchasedMapper);

        when(purchaseMapper.toDTO(any(Purchase.class))).thenReturn(purchaseDTO);
        PurchaseDTO result = purchaseService.updatePurchase(purchaseId, createPurchaseDTO);

        assertEquals(purchaseDTO, result);
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
