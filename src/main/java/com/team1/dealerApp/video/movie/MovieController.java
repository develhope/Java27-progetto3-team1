package com.team1.dealerApp.video.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class MovieController {

    private final MovieService movieService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/a/movies")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movieDTO) throws BadRequestException {
        return ResponseEntity.status(200).body(movieService.addMovie(movieDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/a/movies/{movieId}")
    public ResponseEntity<MovieDTO> deleteMovieById(@RequestParam("movieId") Long movieId) {
        movieService.deleteMovieById(movieId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/u/movies")
    public ResponseEntity<List<MovieDTO>> getAllMovies() throws NoSuchElementException {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/u/movies/{movieId}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable("movieId") Long movieId) throws NoSuchElementException {
        return ResponseEntity.ok(movieService.getMovieDTOById(movieId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("a/movies/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable("movieId") Long movieId, @RequestBody MovieDTO movieDTO) throws NoSuchElementException {
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/a/movies/{movieId}")
    public ResponseEntity<MovieDTO> updateMovieField(@PathVariable("movieId") Long movieId, @RequestParam(name = "field") String fieldName, @RequestBody Object value) throws BadRequestException {
        return ResponseEntity.ok(movieService.updateMovieField(movieId, value, fieldName));
    }

}
