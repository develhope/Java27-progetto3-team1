package com.team1.dealerApp.video.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
@Component
@RequiredArgsConstructor
public class MovieUpdater <T>{
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public Movie updateMovieField(Movie movie, String fieldName, T value) throws NoSuchFieldException, IllegalAccessException {
        Field field = Movie.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(movie, value);
        return movie;
    }
}
