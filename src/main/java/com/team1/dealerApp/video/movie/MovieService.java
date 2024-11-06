package com.team1.dealerApp.video.movie;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MovieUpdater<Object> movieUpdater;

    public MovieDTO addMovie(CreateMovieDTO movieDTO) throws BadRequestException {
        if (movieRepository.existsByTitleAndDirector(movieDTO.getTitle(), movieDTO.getDirector())) {
            throw new BadRequestException("This movie already exists");
        }
        Movie added = movieRepository.save(movieMapper.toMovie(movieDTO));
        return movieMapper.toMovieDTO(added);
    }

    public void deleteMovieById(Long id) {
        movieRepository.deleteById(id);
    }

    public List<MovieDTO> getAllMovies() throws NoSuchElementException {
        List<Movie> movies = movieRepository.findAll();
        if (!movies.isEmpty()) {
            return movies.stream().map(movieMapper::toMovieDTO).toList();
        } else {
            throw new NoSuchElementException("There are no film!");
        }
    }

    public MovieDTO getMovieDTOById(Long movieId) {
        return movieMapper
                .toMovieDTO(movieRepository
                        .findById(movieId)
                        .orElseThrow(() -> new NoSuchElementException("There is no film with id " + movieId)
                        )
                );
    }

    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new NoSuchElementException("There is no film with id " + movieId));
    }

    public MovieDTO updateMovie(Long movieId, MovieDTO movieDTO) throws NoSuchElementException {
        if (movieRepository.existsById(movieId)) {
            Movie movieToUpdate = movieMapper.toMovie(movieDTO);
            movieToUpdate.setId(movieId);
            movieRepository.save(movieToUpdate);
            return movieMapper.toMovieDTO(movieToUpdate);
        }
        throw new NoSuchElementException("There is no movie with id " + movieId);

    }

    public MovieDTO updateMovieField(Long id, Object value, String field) throws BadRequestException {
        Movie movie = movieRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException("No movie with id: " + id));
        Movie updated = new Movie();

        try {
            updated = movieUpdater.updateMovieField(movie, field, value);
        } catch (NoSuchFieldException e) {
            log.error("Error: the field {} does not exist: {}", field, e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("Error to access to the class: {}", e.getMessage());
        }
        movieRepository.save(updated);
        return movieMapper.toMovieDTO(updated);
    }

    public List<Movie> getAllMoviesById(List<Long> idList) {
        return idList.stream().map(this::getMovieById).toList();
    }

    public AdminMovieDTO getSalesById(Long movieId) throws NoSuchElementException {
        Movie moviefound = movieRepository.findById(movieId).orElseThrow(()-> new NoSuchElementException("There is no movie with id " + movieId));
        return movieMapper.toAdminMovieDTO(moviefound);
    }

    public List<AdminMovieDTO> getSales() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(movieMapper::toAdminMovieDTO).toList();
    }
}
