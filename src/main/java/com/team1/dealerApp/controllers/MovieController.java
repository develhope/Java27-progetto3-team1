package com.team1.dealerApp.controllers;

import com.team1.dealerApp.models.dtos.MovieDTO;
import com.team1.dealerApp.services.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // - Modifica film - GetMovieById/GetAllMovies
    @DeleteMapping("/{id}")
    public ResponseEntity<MovieDTO> deleteMovieById(@RequestParam ("id") Long id){
        movieService.deleteMovieById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<?> getAllMovies(){
        try{
            return ResponseEntity.ok(movieService.getAllMovies());
        } catch (BadRequestException e) {
            log.error("Error in get all movies {}", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

}
