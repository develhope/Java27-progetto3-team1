package com.team1.dealerApp.services;


import com.team1.dealerApp.entities.Movie;
import com.team1.dealerApp.mappers.MovieMapper;
import com.team1.dealerApp.models.dtos.MovieDTO;
import com.team1.dealerApp.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieDTO addMovie(MovieDTO movieDTO) {
        movieRepository.save(movieMapper.toMovie(movieDTO));
        return movieDTO;
    }

    public void deleteMovieById(Long id) {
        movieRepository.deleteById(id);
    }

    public List<MovieDTO> getAllMovies() throws BadRequestException {
        List<Movie> movies = movieRepository.findAll();
        if(!movies.isEmpty()){
            return movies.stream().map(movieMapper::toMovieDTO).toList();
        } else{
            throw new BadRequestException("There are no film!");
        }
    }
}
