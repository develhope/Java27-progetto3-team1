package com.team1.dealerApp.rental;


import com.team1.dealerApp.user.Role;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.video.AgeRating;
import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
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


import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


class RentalServiceTest {


    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private RentalService rentalService;

    private Rental rental;
    private RentalDTO rentalDTO;
    private CreateRentalDTO createRentalDTO;
    private Long rentalId;
    private User userCompleteTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        Movie rentableMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.R, 110);
        Movie rentableMovie2 = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2,"Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.G, 110);
        List<Movie> movieList = new ArrayList<>();
        movieList.add(rentableMovie);
        movieList.add(rentableMovie2);


        List <String> castShow= Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List <String> castShow2= Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");
        TvShow rentableShow = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.NC17, 6, 52);
        TvShow rentableShow2 = new TvShow("The Boys", Genre.ACTION, castShow2, "Erik Kripke", Year.of(2019), 60.0, 10.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE,0, 0.0, AgeRating.PG, 5, 40);
        List<TvShow> tvShowList =  new ArrayList<>();
        tvShowList.add(rentableShow);
        tvShowList.add(rentableShow2);


        rentalId = 1L;

        rental = new Rental();
        rental.setId(rentalId);
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(LocalDateTime.now().plusDays(14));
        rental.setRentalStatus(RentalStatus.ACTIVE);
        rental.setMovies(movieList);
        rental.setTvShows(tvShowList);
        rental.setRentalPrice(20.00);

        createRentalDTO = new CreateRentalDTO();
        createRentalDTO.setTvShows(tvShowList);
        createRentalDTO.setMovies(movieList);

        rentalDTO = new RentalDTO();
        rentalDTO.setId(rentalId);
        rentalDTO.setRentalPrice(40.00);
        rentalDTO.setRentalStatus(RentalStatus.ACTIVE);
        rentalDTO.setMovies(movieList);
        rentalDTO.setTvShows(tvShowList);
        rentalDTO.setStartDate(LocalDateTime.now());
        rentalDTO.setEndDate(LocalDateTime.now().plusDays(14));
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

        //return userCompleteTest;


         return  userCompleteTest;
    }

    @Test
    void testAddRental_ThrowsBadRequestException() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        createRentalDTO.setMovies(Collections.emptyList());
        createRentalDTO.setTvShows(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> rentalService.addRental(user, createRentalDTO));
    }
    @Test
    void testAddRental() throws BadRequestException {
        User user = defaultUser(new ArrayList<>(), new ArrayList<>());
        when(rentalMapper.toRental(createRentalDTO)).thenReturn(rental);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);
        when(userService.getUserByEmail(user)).thenReturn(user);
        rentalService.addRental(user, createRentalDTO);

        assertEquals(40.0, rental.getRentalPrice());  // Assert the rental price is correct
    }

    @Test
    void testGetAllRentalByUserId_ThrowsNoSuchElementException() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        Pageable pageable = PageRequest.of(0, 10);
        when(rentalRepository.findByRenter_Email(user.getUsername(), pageable)).thenReturn(Page.empty());
        assertThrows(NoSuchElementException.class, () -> rentalService.getActiveUserRentals(user, 0, 10));
    }

    @Test
    void testGetAllRentalByUserId() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Rental> rentalPage =  new PageImpl<>(List.of(rental));
        when(rentalRepository.findByRenter_Email(user.getUsername(), pageable)).thenReturn((rentalPage));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        Page<RentalDTO> result = rentalService.getActiveUserRentals(user, 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateRentalEndDate() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        LocalDateTime newEndDate = LocalDateTime.now().plusDays(30);
        when(rentalRepository.findByRenter_EmailAndId(eq(user.getUsername()), anyLong()))
                        .thenReturn(Optional.of(rental));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        rentalService.updateRentalEndDate(user, rentalId, LocalDateTime.now().plusDays(30));

        assertEquals(newEndDate.truncatedTo(ChronoUnit.MINUTES), rental.getEndDate().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void testUpdateRentalEndDate_ThrowsNoSuchElementException() {
        UserDetails user = defaultUser(new ArrayList<>(), new ArrayList<>());
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rentalService.updateRentalEndDate(user,rentalId, LocalDateTime.now()));
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
