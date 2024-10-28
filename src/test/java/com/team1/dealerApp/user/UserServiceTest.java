package com.team1.dealerApp.user;

import com.team1.dealerApp.rental.Rental;
import com.team1.dealerApp.rental.RentalStatus;

import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;


    private UUID userId;
    private User userCompleteTest;
    private CreateUserDTO createUserDTOCompleteTest;
    private UserDTO userDTOCompleteTest;
    private Rental rentalCompleteTest;


    //Entities
    public User defaultUser(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        userCompleteTest = new User();

        UUID userId = UUID.randomUUID();

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

    public CreateUserDTO defaultCreateUserDTO(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        createUserDTOCompleteTest = new CreateUserDTO();

        createUserDTOCompleteTest.setFirstName("Mario");
        createUserDTOCompleteTest.setLastName("Rossi");
        createUserDTOCompleteTest.setEmail("mario.rossi@gmail.com");
        createUserDTOCompleteTest.setPhoneNumber("3331234567");
        createUserDTOCompleteTest.setSubscriptionStatus(SubscriptionStatus.FULL_SUBSCRIPTION);
        createUserDTOCompleteTest.setWatchedMovies(defaultMovieList);
        createUserDTOCompleteTest.setWatchedShows(defaultTvShowList);

        return createUserDTOCompleteTest;
    }

    public Rental defaultRental(List<Movie> defaultMovieList, List<TvShow> defaultTvShowList) {

        Long rentalId = 1L;

        rentalCompleteTest = new Rental();

        rentalCompleteTest.setId(rentalId);
        rentalCompleteTest.setStartDate(LocalDateTime.now());
        rentalCompleteTest.setEndDate(LocalDateTime.now().plusDays(14));
        rentalCompleteTest.setRentalStatus(RentalStatus.ACTIVE);
        rentalCompleteTest.setMovies(defaultMovieList);
        rentalCompleteTest.setTvShows(defaultTvShowList);
        rentalCompleteTest.setRentalPrice(20.00);

        return rentalCompleteTest;
    }


    //Lists
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


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        userCompleteTest = defaultUser(defaultMovieList(), defaultTvShowList());
        createUserDTOCompleteTest = defaultCreateUserDTO(defaultMovieList(), defaultTvShowList());
        userDTOCompleteTest = new UserDTO();
        userDTOCompleteTest = new UserDTO();

    }


    //Test per getUserDTOById if-> NoSuchElementException
    @Test
    void testGetUserDTOById_NoSuchElementException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> userService.getUserDTOById(userId));

        assertEquals("User with Id " + userId + " not found", exception.getMessage());
    }

    //Test per getUserDTOById if-> User Found
    @Test
    void testGetUserDTOById_UserFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userCompleteTest));

        when(userMapper.toUserDTO(userCompleteTest)).thenReturn(userDTOCompleteTest);

        UserDTO resultUser = userService.getUserDTOById(userId);

        assertEquals(userDTOCompleteTest, resultUser);
    }


    //Test per updateUser if-> NoSuchElementException
    @Test
    void testUpdateUser_NoSuchElementException() {
        when(userRepository.findByEmail(userCompleteTest.getEmail())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> userService.updateUser(userCompleteTest, createUserDTOCompleteTest));

        assertEquals("This User doesn't exist", exception.getMessage());
    }

    //Test per updateUser if-> Done Updated
    @Test
    void testUpdateUser_DoneUpdated() {
        when(userRepository.findByEmail(userCompleteTest.getEmail())).thenReturn(Optional.of(userCompleteTest));

        User userToUpdate = new User();
        when(userMapper.toUser(createUserDTOCompleteTest)).thenReturn(userToUpdate);

        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);
        when(userMapper.toUserDTO(userToUpdate)).thenReturn(userDTOCompleteTest);

        UserDTO resultUserDTO = userService.updateUser(userCompleteTest, createUserDTOCompleteTest);

        assertEquals(userDTOCompleteTest, resultUserDTO);
    }


    //Test per deleteUser
    @Test
    void testDeleteUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userCompleteTest));

        userRepository.deleteById(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> deletedUser = userRepository.findById(userId);

        assertTrue(deletedUser.isEmpty());
    }

}
