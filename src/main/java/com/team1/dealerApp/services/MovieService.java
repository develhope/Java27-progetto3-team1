package com.team1.dealerApp.services;


import com.team1.dealerApp.mappers.MovieMapper;
import com.team1.dealerApp.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;


}
