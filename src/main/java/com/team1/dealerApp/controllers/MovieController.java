package com.team1.dealerApp.controllers;

import com.team1.dealerApp.models.dtos.MovieDTO;
import com.team1.dealerApp.services.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @PostMapping()
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movieDTO){
        return ResponseEntity.status(200).body(movieService.addMovie(movieDTO));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<MovieDTO> deleteMovieById(@RequestParam ("movieId") Long movieId){
        movieService.deleteMovieById(movieId);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<?> getAllMovies(){
        try{
            return ResponseEntity.ok(movieService.getAllMovies());
        } catch (BadRequestException e) {
            log.error("Error in get all movies {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<?> getMovieById(@PathVariable ("movieId") Long movieId){
        try{
            return ResponseEntity.ok(movieService.getMovieById(movieId));
        } catch (NoSuchElementException e){
            log.error("Error in get movie by Id {} : {}", movieId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<?> updateMovie(@PathVariable ("movieId") Long movieId, @RequestBody MovieDTO movieDTO){
        try{
            return ResponseEntity.ok(movieService.updateMovie(movieId,movieDTO));
        } catch (BadRequestException e){
            log.error("Error in update movie by Id {} : {}",movieId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
