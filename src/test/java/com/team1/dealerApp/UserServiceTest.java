package com.team1.dealerApp;

import com.team1.dealerApp.rental.Rental;
import com.team1.dealerApp.rental.RentalStatus;
import com.team1.dealerApp.user.*;

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
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;


    private User user;
    private UserDTO userDTO;
    private CreateUserDTO createUserDTO;
    private UUID userId;
    private Rental rental;
    private Long rentalId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        Movie whatchedMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 110);
        Movie whatchedMovie2 = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 110);
        List<Movie> whatchedMovieList = new ArrayList<>();
        whatchedMovieList.add(whatchedMovie);
        whatchedMovieList.add(whatchedMovie2);

        List<String> castShow = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List<String> castShow2 = Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");
        TvShow whatchedTvShow = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 6, 52);
        TvShow whatchedTvShow2 = new TvShow("The Boys", Genre.ACTION, castShow2, "Erik Kripke", Year.of(2019), 60.0, 10.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 5, 40);
        List<TvShow> whatchedTvShowList = new ArrayList<>();
        whatchedTvShowList.add(whatchedTvShow);
        whatchedTvShowList.add(whatchedTvShow2);


        user = new User();

        userId = UUID.randomUUID();

        user.setId(userId);
        user.setFirstName("Simone");
        user.setLastName("Cuccu");
        user.setEmail("simone.cuccu3@gmail.com");
        user.setPhoneNumber("3331234567");
        user.setSubscriptionStatus(SubscriptionStatus.FULL_SUBSCRIPTION);
        user.setWatchedMovies(whatchedMovieList);
        user.setWatchedShows(whatchedTvShowList);


        createUserDTO = new CreateUserDTO();

        createUserDTO.setFirstName("Simone");
        createUserDTO.setLastName("Cuccu");
        createUserDTO.setEmail("simone.cuccu3@gmail.com");
        createUserDTO.setPhoneNumber("3331234567");
        createUserDTO.setSubscriptionStatus(SubscriptionStatus.FULL_SUBSCRIPTION);
        createUserDTO.setWatchedMovies(whatchedMovieList);
        createUserDTO.setWatchedShows(whatchedTvShowList);


        rentalId = 1L;

        rental = new Rental();
        rental.setId(rentalId);
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(LocalDateTime.now().plusDays(14));
        rental.setRentalStatus(RentalStatus.ACTIVE);
        rental.setMovies(whatchedMovieList);
        rental.setTvShows(whatchedTvShowList);
        rental.setRentalPrice(20.00);

        List<Rental> rentalList = new ArrayList<>();
        rentalList.add(rental);

        user.setRentals(rentalList);

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
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO resultUser = userService.getUserDTOById(userId);

        assertEquals(userDTO, resultUser);
    }


    //Test per updateUser if-> NoSuchElementException
    @Test
    void testUpdateUser_NoSuchElementException() {
        when(userRepository.existsById(userId)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> userService.updateUser(userId, createUserDTO));

        assertEquals("This User doesn't exist", exception.getMessage());
    }

    //Test per updateUser if-> Done Updated
    @Test
    void testUpdateUser_DoneUpdated() {
        when(userRepository.existsById(userId)).thenReturn(true);

        User userToUpdate = new User();
        when(userMapper.toUser(createUserDTO)).thenReturn(userToUpdate);
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);

        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO resultUserDTO = userService.updateUser(userId, createUserDTO);

        assertEquals(userDTO, resultUserDTO);

    }


    //Test per deeleteUser
    @Test
    void testDeleteUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userRepository.deleteById(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> deletedUser = userRepository.findById(userId);
        assertTrue(deletedUser.isEmpty());
    }

}
