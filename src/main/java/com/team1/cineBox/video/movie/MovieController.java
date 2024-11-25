package com.team1.cineBox.video.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class MovieController {

    private final MovieService movieService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/a/movies")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody CreateMovieDTO movieDTO) throws BadRequestException {
        return ResponseEntity.status(200).body(movieService.addMovie(movieDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/a/movies/{movieId}")
    public ResponseEntity<MovieDTO> deleteMovieById(@PathVariable("movieId") Long movieId) {
        movieService.deleteMovieById(movieId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/u/movies")
    public ResponseEntity< Page <MovieDTO> > getAllMovies( @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) throws NoSuchElementException {
        return ResponseEntity.ok(movieService.getAllMovies(page,size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/u/movies/{movieId}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable("movieId") Long movieId) throws NoSuchElementException {
        return ResponseEntity.ok(movieService.getMovieDTOById(movieId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("a/movies/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable("movieId") Long movieId, @RequestBody CreateMovieDTO movieDTO) throws NoSuchElementException {
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/a/movies/{movieId}")
    public ResponseEntity<MovieDTO> updateMovieField( @PathVariable Long movieId, @RequestBody Map <String, Object> fieldValueMap) throws BadRequestException {
        return ResponseEntity.ok(movieService.updateMovieField(fieldValueMap, movieId));
    }

    @GetMapping("/a/movies/sales/{movieId}")
    public ResponseEntity<AdminMovieDTO> getSalesById(@PathVariable("movieId") Long movieId) throws NoSuchElementException{
        return ResponseEntity.ok(movieService.getSalesById(movieId));
    }

    @GetMapping("/a/movies/sales")
    public ResponseEntity<Page<AdminMovieDTO>> getSales(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(movieService.getSales(page,size));
    }

}
