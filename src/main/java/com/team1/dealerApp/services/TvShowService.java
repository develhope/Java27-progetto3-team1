package com.team1.dealerApp.services;

import com.team1.dealerApp.entities.TvShow;
import com.team1.dealerApp.mappers.TvShowMapper;
import com.team1.dealerApp.models.TvShowUpdater;
import com.team1.dealerApp.models.dtos.TvShowDTO;
import com.team1.dealerApp.repositories.TvShowRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class TvShowService {
	private final TvShowRepository tvShowRepository;
	private final TvShowMapper tvShowMapper;
	private final TvShowUpdater<?> tvShowUpdater;

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

	public TvShowDTO getShowById (Long id) throws BadRequestException {
		TvShow tvShow = tvShowRepository
				.findById(id)
				.orElseThrow(() -> new BadRequestException("No show with id: " + id));
		return  tvShowMapper.toTvShowDTO(tvShow);
	}

	public TvShowDTO addTvShow (TvShowDTO tvShowDTO){
		tvShowRepository.save(tvShowMapper.toTvShow(tvShowDTO));
		return tvShowDTO;
	}

	public TvShowDTO updateShow (TvShowDTO tvShowDTO, Long id) throws BadRequestException {
		TvShow found = tvShowMapper.toTvShow(getShowById(id));
		found = tvShowMapper.toTvShow(tvShowDTO);
		found.setId(id);
		tvShowRepository.save(found);
		return tvShowMapper.toTvShowDTO(found);
	}

	public TvShowDTO updateShowField (Long id, double newPrice, String field) throws BadRequestException {
		TvShow tvShow = tvShowRepository
				.findById(id)
				.orElseThrow(() -> new BadRequestException("No show with id: " + id))
	}
}
