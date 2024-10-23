package com.team1.dealerApp.video.tvshow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class TvShowUpdater <T>{

	private final TvShowRepository tvShowRepository;
	private final TvShowMapper tvShowMapper;

	public TvShow updateShowField( TvShow tvShow, String fieldName, T value) throws NoSuchFieldException, IllegalAccessException {
		Field field = TvShow.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(tvShow, value );
		return tvShow;
	}
}
