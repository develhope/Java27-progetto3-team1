package com.team1.cineBox.video;

import com.team1.cineBox.video.movie.MovieMapper;
import com.team1.cineBox.video.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class FieldUpdater <T> {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public <K extends Video> K updateField( K video, String fieldName, T value, Class < ? > className  ) throws NoSuchFieldException, IllegalAccessException {
        Class < ? > currentClass = className;

        Field field = null;
        while ( currentClass != null ) {
            try {
                // Tenta di ottenere il campo dalla classe corrente
                field = currentClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                break;
            } catch ( NoSuchFieldException e ) {
                currentClass = currentClass.getSuperclass();
            }
        }
        field.set(video, value);
        field.setAccessible(false);
        return video;
    }
}
