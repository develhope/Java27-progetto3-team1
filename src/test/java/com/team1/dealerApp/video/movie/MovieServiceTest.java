package com.team1.dealerApp.video.movie;


import com.team1.dealerApp.video.AgeRating;
import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    private final MovieMapper movieMapper = new MovieMapper();

    // Simula il MovieRepository
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapperInj;

    // Simula il MovieUpdater
    @Mock
    private MovieUpdater<Object> movieUpdater;

    // Inietta il MovieService da testare
    @InjectMocks
    private MovieService movieService;



    private MovieDTO purchasableMovieDTO;
    private Movie purchasableMovie;
    private Movie rentableMovie;

    @BeforeEach
    public void setup() {
        // Inizializza i mock prima di ogni test
        MockitoAnnotations.openMocks(this);
        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");

        purchasableMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.PURCHASABLE, 0, 0.0, AgeRating.PG, 110);
        rentableMovie = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2,"Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.NC17, 110);

        purchasableMovieDTO = movieMapper.toMovieDTO(purchasableMovie);

        purchasableCreateMovieDTO = new CreateMovieDTO("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.PURCHASABLE, 0, 0.0, AgeRating.PG, 110);

    }

    @Test
    public void testAddMovie() throws BadRequestException {
        //Specifica cosa fare quando viene invocato quel metodo findMovieByTitleAndDirector (contenuto in addMovie)
        when(movieRepository.findMovieByTitleAndDirector(purchasableMovie.getTitle(), purchasableMovie.getDirector())).thenReturn(null);

        MovieDTO result = movieService.addMovie(purchasableCreateMovieDTO);

        // Verifica che il DTO restituito sia quello originale
        assertEquals(purchasableMovieDTO, result);
    }


    @Test
    public void testAddMovie_whenMovieAlreadyExists() {
        when(movieRepository.findMovieByTitleAndDirector(purchasableMovieDTO.getTitle(), purchasableMovieDTO.getDirector())).thenReturn(purchasableMovie);
        assertThrows(BadRequestException.class, () -> movieService.addMovie(purchasableMovieDTO));
    }

    @Test
    public void testDeleteMovieById() {
        movieService.deleteMovieById(1L);
        // Verifica che il film venga cancellato correttamente
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllMovies() {

        when(movieRepository.findAll()).thenReturn(Arrays.asList(purchasableMovie, rentableMovie));

        List<MovieDTO> result = movieService.getAllMovies();

        // Verifica che vengano restituiti due film
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllMovies_NoMoviesFound() {

        when(movieRepository.findAll()).thenReturn(new ArrayList<>());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> movieService.getAllMovies());

        // Verifica che il messaggio di errore sia corretto
        assertEquals("There are no film!", exception.getMessage());
    }

    @Test
    public void testGetMovieDTOById() {
        // con anyLong() si assegna un qualsiasi valore Long visto che movie non ha ancora un Id
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(purchasableMovie));

         movieService.getMovieDTOById(1L);

        // Verifica che il repository venga chiamato
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMovieDTOById_NotFound() {

        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> movieService.getMovieDTOById(1L));

        assertEquals("There is no film with id 1", exception.getMessage());
    }

    @Test
    public void testUpdateMovie() {
        purchasableMovieDTO.setTitle("Il signore degli anelli: le due torri");

        when(movieRepository.existsById(1L)).thenReturn(true);
        when(movieMapperInj.toMovie(purchasableMovieDTO)).thenReturn(purchasableMovie);
        when(movieMapperInj.toMovieDTO(purchasableMovie)).thenReturn(purchasableMovieDTO);

        MovieDTO result = movieService.updateMovie(1L, purchasableMovieDTO);

        assertEquals("Il signore degli anelli: le due torri", result.getTitle());
    }

    @Test
    public void testUpdateMovie_NotFound() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> movieService.updateMovie(1L, purchasableMovieDTO));

        assertEquals("There is no movie with id 1", exception.getMessage());
    }

    @Test
    public void testUpdateMovieField() throws Exception {

        when(movieRepository.findById(1L)).thenReturn(Optional.of(purchasableMovie));

        Movie updatedMovie = new Movie();
        when(movieUpdater.updateMovieField(any(Movie.class), anyString(), any())).thenReturn(updatedMovie);
        when(movieMapperInj.toMovieDTO(updatedMovie)).thenReturn(new MovieDTO());

        movieService.updateMovieField(1L, "New Title", "title");

        // Verifica che il film aggiornato sia salvato
        verify(movieRepository, times(1)).save(updatedMovie);
    }

    @Test
    public void testUpdateMovieField_NoMovieFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> movieService.updateMovieField(1L, "New Title", "title"));

        assertEquals("No movie with id: 1", exception.getMessage());
    }

  
}