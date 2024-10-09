package com.team1.dealerApp.video.movie;


import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

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
    Movie movie;
    Movie movie2;

    @BeforeEach
    public void setup() {
        // Inizializza i mock prima di ogni test
        MockitoAnnotations.openMocks(this);

        List<String> castMovieDTO = Arrays.asList("Natalie Portman", "Rupert Grace", "Ben Miles");
        movieDTO = new MovieDTO("V per Vendetta", 120, Genre.ACTION, castMovieDTO, "James McTeigue", Year.of(2005), 50.00, 24.00, "V per Vendetta", 5.0f, VideoStatus.PURCHASABLE);

        List<String> castMovie = Arrays.asList("Elijah Wood", "Ian McKellan", "Orlando Bloom");
        movie = new Movie("Il signore degli anelli: il ritorno del Re", Genre.FANTASY, castMovie, "Peter Jackson", Year.of(2003), 30.00, 10.00, "Il ritorno del Re", 4.0f, VideoStatus.PURCHASABLE, 110);

        List<String> castMovie2 = Arrays.asList("Ed Wynn", "Richard Haydn", "Kathryn Beaumont");
        movie2 = new Movie("Alice nel paese delle Meraviglie", Genre.FANTASY, castMovie, "Clyde Geronimi", Year.of(1951), 30.00, 10.00, "Alice nel paese delle meraviglie", 2.0f, VideoStatus.RENTABLE, 110);

    }


  
}