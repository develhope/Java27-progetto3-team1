package com.team1.dealerApp.video.movie;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class MovieUpdater <T>{
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public Movie updateMovieField(Movie movie, String fieldName, T value) throws NoSuchFieldException, IllegalAccessException {
        Field field = Movie.class.getField(fieldName);
        field.setAccessible(true);
        field.set(movie, value);
        return movie;
    }
}
