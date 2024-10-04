package com.team1.dealerApp.controllers;

import com.team1.dealerApp.models.dtos.MovieDTO;
import com.team1.dealerApp.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @PostMapping()
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movieDTO){
        return ResponseEntity.status(200).body(movieService.addMovie(movieDTO));
    }


}
