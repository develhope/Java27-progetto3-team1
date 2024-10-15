package com.team1.dealerApp.rental;


import com.team1.dealerApp.user.UserService;
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


import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private UUID userId;
    private Long rentalId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        Movie rentableMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.RENTABLE, 110);
        Movie rentableMovie2 = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2,"Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 110);
        List<Movie> movieList = new ArrayList<>();
        movieList.add(rentableMovie);
        movieList.add(rentableMovie2);


        List <String> castShow= Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List <String> castShow2= Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");
        TvShow rentableShow = new TvShow("Breaking bad", Genre.DRAMA, castShow, "Vince Gilligan", Year.of(2006), 50.0, 10.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.RENTABLE, 6, 52);
        TvShow rentableShow2 = new TvShow("The Boys", Genre.ACTION, castShow2, "Erik Kripke", Year.of(2019), 60.0, 10.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 5, 40);
        List<TvShow> tvShowList =  new ArrayList<>();
        tvShowList.add(rentableShow);
        tvShowList.add(rentableShow2);


        userId = UUID.randomUUID();
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

    @Test
    void testAddRental_ThrowsBadRequestException() {
        createRentalDTO.setMovies(Collections.emptyList());
        createRentalDTO.setTvShows(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> rentalService.addRental(userId, createRentalDTO));
    }
    @Test
    void testAddRental() throws BadRequestException {

        when(rentalMapper.toRental(createRentalDTO)).thenReturn(rental);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);
        RentalDTO result = rentalService.addRental(userId, createRentalDTO);

        assertEquals(40.0, rental.getRentalPrice());  // Assert the rental price is correct
    }

    @Test
    void testGetAllRentalByUserId_ThrowsNoSuchElementException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(rentalRepository.findByRenterId(userId, pageable)).thenReturn(Page.empty());
        assertThrows(NoSuchElementException.class, () -> rentalService.getAllRentalByUserId(userId, 0, 10));
    }

    @Test
    void testGetAllRentalByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rental> rentalPage = new PageImpl<>(List.of(rental));
        when(rentalRepository.findByRenterId(userId, pageable)).thenReturn(rentalPage);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        Page<RentalDTO> result = rentalService.getAllRentalByUserId(userId, 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateRentalEndDate() {
        LocalDateTime newEndDate = LocalDateTime.now().plusDays(30);
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        RentalDTO result = rentalService.updateRentalEndDate(rentalId, newEndDate);

        assertEquals(newEndDate, rental.getEndDate());
    }

    @Test
    void testUpdateRentalEndDate_ThrowsNoSuchElementException() {
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rentalService.updateRentalEndDate(rentalId, LocalDateTime.now()));
    }
}
