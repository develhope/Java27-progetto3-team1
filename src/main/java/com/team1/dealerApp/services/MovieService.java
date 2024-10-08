package com.team1.dealerApp.services;


import com.team1.dealerApp.entities.Movie;
import com.team1.dealerApp.mappers.MovieMapper;
import com.team1.dealerApp.models.MovieUpdater;
import com.team1.dealerApp.models.dtos.MovieDTO;
import com.team1.dealerApp.repositories.MovieRepository;
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

    public MovieDTO addMovie(MovieDTO movieDTO) throws BadRequestException {
        Movie movieFound = movieRepository.findMovieByTitleAndDirector(movieDTO.getTitle(), movieDTO.getDirector());
        if(movieFound == null){
            movieRepository.save(movieMapper.toMovie(movieDTO));
        } else {
            throw new BadRequestException("This movie already exists");
        }
        return movieDTO;
    }

    public void deleteMovieById(Long id) {
        movieRepository.deleteById(id);
    }

    public List<MovieDTO> getAllMovies() throws NoSuchElementException {
        List<Movie> movies = movieRepository.findAll();
        if(!movies.isEmpty()){
            return movies.stream().map(movieMapper::toMovieDTO).toList();
        } else{
            throw new NoSuchElementException("There are no film!");
        }
    }

    public MovieDTO getMovieById(Long movieId) {
       return movieMapper
               .toMovieDTO(movieRepository
                       .findById(movieId)
                       .orElseThrow(()-> new NoSuchElementException("There is no film with id " + movieId)
                       )
               );
    }

    public MovieDTO updateMovie(Long movieId, MovieDTO movieDTO) throws NoSuchElementException {
        if(movieRepository.existsById(movieId)){
            Movie movieToUpdate = movieMapper.toMovie(movieDTO);
            movieToUpdate.setId(movieId);
            movieRepository.save(movieToUpdate);
        } throw new NoSuchElementException("There is no movie with id " + movieId);

    }

    public MovieDTO updateMovieField ( Long id, Object value, String field ) throws BadRequestException {
        Movie tvShow = movieRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException("No movie with id: " + id));
        Movie updated = new Movie();

        try {
            updated = movieUpdater.updateMovieField(tvShow, field, value);
        }catch ( NoSuchFieldException e ){
            log.error("Error: the field {} does not exist: {}", field, e.getMessage());
        }catch ( IllegalAccessException e){
            log.error("Error to access to the class: {}", e.getMessage());
        }
        movieRepository.save(updated);
        return movieMapper.toMovieDTO(updated);
    }
}
