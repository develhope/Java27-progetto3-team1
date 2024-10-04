package com.team1.dealerApp.models;

import com.team1.dealerApp.entities.TvShow;
import com.team1.dealerApp.mappers.TvShowMapper;
import com.team1.dealerApp.repositories.TvShowRepository;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class TvShowUpdater <T>{

	private final TvShowRepository tvShowRepository;
	private final TvShowMapper tvShowMapper;

	public TvShow updateShowField( TvShow tvShow, String fieldName, T value) throws NoSuchFieldException, IllegalAccessException {
		Field field = TvShow.class.getField(fieldName);
		field.setAccessible(true);
		field.set(tvShow, value );
		return tvShow;
	}
}
