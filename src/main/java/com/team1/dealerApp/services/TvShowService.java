package com.team1.dealerApp.services;

import com.team1.dealerApp.entities.TvShow;
import com.team1.dealerApp.mappers.TvShowMapper;
import com.team1.dealerApp.models.dtos.TvShowDTO;
import com.team1.dealerApp.repositories.TvShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TvShowService {
	private final TvShowRepository tvShowRepository;
	private final TvShowMapper tvShowMapper;

	public List <TvShowDTO> getAllShows () throws BadRequestException {
		List <TvShow> tvShows = tvShowRepository.findAll();
		if (!tvShows.isEmpty()){
			return tvShows
					.stream()
					.map(tvShowMapper::toTvShowDTO)
					.toList();
		}else{
			throw new BadRequestException("There are no shows!");
		}
	}
}
