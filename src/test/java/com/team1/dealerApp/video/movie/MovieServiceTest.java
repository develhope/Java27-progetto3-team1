package com.team1.dealerApp.video.movie;


import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    // Simula il MovieRepository
    @Mock
    private MovieRepository movieRepository;

    // Simula il MovieMapper
    @Mock
    private MovieMapper movieMapper;

    // Simula il MovieUpdater
    @Mock
    private MovieUpdater<Object> movieUpdater;

    // Inietta il MovieService da testare
    @InjectMocks
    private MovieService movieService;

    MovieDTO movieDTO;
    MovieDTO movieDTO2;
    Movie movie;
    Movie movie2;

    @BeforeEach
    public void setup() {
        // Inizializza i mock prima di ogni test
        MockitoAnnotations.openMocks(this);
        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");

        movieDTO = new MovieDTO("Il signore degli anelli: il ritorno del Re", 110, Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.PURCHASABLE);
        movieDTO2 = new MovieDTO("Alice nel paese delle Meraviglie", 110, Genre.ANIMATION, castMovie2,"Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE);

        movie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.PURCHASABLE, 110);

        movie2 = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 110);
    }

    @Test
    public void testAddMovie() throws BadRequestException {

        //Specifica cosa fare quando viene invocato quel metodo findMovieByTitleAndDirector (contenuto in addMovie)
        when(movieRepository.findMovieByTitleAndDirector(movieDTO.getTitle(), movieDTO.getDirector())).thenReturn(null);

        //Specifica cosa fare quando viene invocato il metodo toMovie
        when(movieMapper.toMovie(movieDTO)).thenReturn(movie);

        MovieDTO result = movieService.addMovie(movieDTO);
        assertNotNull(result);

        // Verifica che il DTO restituito sia quello originale
        assertEquals(movieDTO, result);

        // Verifica che il film sia stato salvato
        verify(movieRepository, times(1)).save(any(Movie.class));
    }


    @Test
    public void testAddMovie_MovieAlreadyExists() {
        when(movieRepository.findMovieByTitleAndDirector(movieDTO.getTitle(), movieDTO.getDirector())).thenReturn(movie);
        assertThrows(BadRequestException.class, () -> movieService.addMovie(movieDTO));

    // Verifica che non salvi un film già esistente
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testDeleteMovieById() {
        movieService.deleteMovieById(1L);

        // Verifica che il film venga cancellato correttamente
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllMovies() {

        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie, movie2));

        when(movieMapper.toMovieDTO(movie)).thenReturn(movieDTO);
        when(movieMapper.toMovieDTO(movie2)).thenReturn(movieDTO2);

        List<MovieDTO> result = movieService.getAllMovies();

        assertNotNull(result);

        // Verifica che vengano restituiti due film
        assertEquals(2, result.size());

        // Verifica che il metodo findAll venga chiamato
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllMovies_NoMoviesFound() {

        when(movieRepository.findAll()).thenReturn(Arrays.asList());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            movieService.getAllMovies();
        });

        // Verifica che il messaggio di errore sia corretto
        assertEquals("There are no film!", exception.getMessage());

        // Verifica che il metodo findAll venga chiamato
        verify(movieRepository, times(1)).findAll();
    }


  
}