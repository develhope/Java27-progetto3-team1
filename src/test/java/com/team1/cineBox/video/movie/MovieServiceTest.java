package com.team1.cineBox.video.movie;

import com.team1.cineBox.utils.Pager;
import com.team1.cineBox.video.*;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
    private FieldUpdater <Object> fieldUpdater;

    @Mock
    private Pager pager;

    // Inietta il MovieService da testare
    @InjectMocks
    private MovieService movieService;

    private MovieDTO purchasableMovieDTO;
    private Movie purchasableMovie;
    private Movie rentableMovie;
    private CreateMovieDTO purchasableCreateMovieDTO;
    private Page<Movie> pageMovie;

    @BeforeEach
    public void setup() {
        // Inizializza i mock prima di ogni test
        MockitoAnnotations.openMocks(this);
        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");

        purchasableMovie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.PURCHASABLE, 0, 0.0, AgeRating.PG, 110);
        rentableMovie = new Movie("Alice nel paese delle Meraviglie", Genre.ANIMATION, castMovie2, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.NC17, 110);

        purchasableMovieDTO = movieMapper.toMovieDTO(purchasableMovie);

        purchasableCreateMovieDTO = new CreateMovieDTO("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.PURCHASABLE, 0, 0.0, AgeRating.PG, 110);

        pageMovie = new PageImpl<>(Collections.singletonList(purchasableMovie));
    }

    @Test
    void testAddMovie() throws BadRequestException {

        //Specifica cosa fare quando viene invocato quel metodo findMovieByTitleAndDirector (contenuto in addMovie)
        when(movieRepository.findMovieByTitleAndDirector(purchasableMovie.getTitle(), purchasableMovie.getDirector())).thenReturn(null);
        when(movieRepository.save(any(Movie.class))).thenReturn(purchasableMovie);
        when(movieMapperInj.toMovie(any(CreateMovieDTO.class))).thenReturn(purchasableMovie);
        when(movieMapperInj.toMovieDTO(any(Movie.class))).thenReturn(purchasableMovieDTO);

        MovieDTO result = movieService.addMovie(purchasableCreateMovieDTO);

        // Verifica che il DTO restituito sia quello originale
        assertEquals(purchasableMovieDTO, result);
    }


    @Test
    void testAddMovie_whenMovieAlreadyExists() {
        when(movieRepository.existsByTitleAndDirector(purchasableMovieDTO.getTitle(), purchasableMovieDTO.getDirector())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> movieService.addMovie(purchasableCreateMovieDTO));
    }

    @Test
    void testDeleteMovieById() {
        movieService.deleteMovieById(1L);
        // Verifica che il film venga cancellato correttamente
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllMovies() {

        int page = 0;
        int size = 10;

        when(movieRepository.findAll()).thenReturn(Arrays.asList(purchasableMovie, rentableMovie));
        when(pager.createPageable(anyInt(), anyInt())).thenReturn(PageRequest.of(page, size));
        when(movieRepository.findAll(pager.createPageable(page, size))).thenReturn(pageMovie);

        Page<MovieDTO> moviePage = movieService.getAllMovies(page, size);
        List<MovieDTO> result = moviePage.getContent();

        // Verifica che vengano restituiti due film
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllMovies_NoMoviesFound() {

        int page = 0;
        int size = 10;

        when(movieRepository.findAll(PageRequest.of(page, size))).thenReturn(Page.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> movieService.getAllMovies(page, size));

        // Verifica che il messaggio di errore sia corretto
        assertEquals("There are no film!", exception.getMessage());
    }

    @Test
    void testGetMovieDTOById() {
        // con anyLong() si assegna un qualsiasi valore Long visto che movie non ha ancora un Id
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(purchasableMovie));

        movieService.getMovieDTOById(1L);

        // Verifica che il repository venga chiamato
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMovieDTOById_NotFound() {

        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> movieService.getMovieDTOById(1L));

        assertEquals("There is no film with id 1", exception.getMessage());
    }

    @Test
    void testUpdateMovie() {
        purchasableMovieDTO.setTitle("Il signore degli anelli: le due torri");

        when(movieRepository.existsById(1L)).thenReturn(true);
        when(movieMapperInj.toMovie(any(CreateMovieDTO.class))).thenReturn(purchasableMovie);
        when(movieRepository.save(any(Movie.class))).thenReturn(purchasableMovie);
        when(movieMapperInj.toMovieDTO(purchasableMovie)).thenReturn(purchasableMovieDTO);

        MovieDTO result = movieService.updateMovie(1L, purchasableCreateMovieDTO);

        assertEquals("Il signore degli anelli: le due torri", result.getTitle());
    }

    @Test
    void testUpdateMovie_NotFound() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> movieService.updateMovie(1L, purchasableCreateMovieDTO));

        assertEquals("There is no movie with id 1", exception.getMessage());
    }


    @Test
    void testUpdateMovieField() throws Exception {

        when(movieRepository.findById(1L)).thenReturn(Optional.of(purchasableMovie));

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("Gli allegri amici");
        when(fieldUpdater.updateField(any(Movie.class), anyString(), any(Object.class), any(Class.class))).thenReturn(updatedMovie);
        when(movieMapperInj.toMovieDTO(updatedMovie)).thenReturn(new MovieDTO());
        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);

        movieService.updateMovieField(Map.of("title", "Gli allegri amici"), 1L);

        // Verifica che il film aggiornato sia salvato
        verify(movieRepository, times(1)).save(updatedMovie);
    }

    @Test
    void testUpdateMovieField_NoMovieFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> movieService.updateMovieField( Map.of("title", "Bel film"), 1L));

        assertEquals("No movie with id: 1", exception.getMessage());
    }

}
