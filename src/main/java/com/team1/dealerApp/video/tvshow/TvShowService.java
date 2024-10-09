package com.team1.dealerApp.video.tvshow;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class TvShowService {
	private final TvShowRepository tvShowRepository;
	private final TvShowMapper tvShowMapper;
	private final TvShowUpdater<Object> tvShowUpdater;

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
		TvShow found = tvShowMapper.toTvShow(tvShowDTO);
		found.setId(id);
		tvShowRepository.save(found);
		return tvShowMapper.toTvShowDTO(found);
	}

	public TvShowDTO updateShowField ( Long id, Object value, String field ) throws BadRequestException {
		TvShow tvShow = tvShowRepository
				.findById(id)
				.orElseThrow(() -> new BadRequestException("No show with id: " + id));
		TvShow updated = new TvShow();

		try {
			updated = tvShowUpdater.updateShowField(tvShow, field, value);
		}catch ( NoSuchFieldException e ){
			log.error("Error: the field {} does not exist: {}", field, e.getMessage());
		}catch ( IllegalAccessException e){
			log.error("Errore nell'accesso alla classe: {}", e.getMessage());
		}
		tvShowRepository.save(updated);
		return tvShowMapper.toTvShowDTO(updated);
	}

	public void deleteShowById ( Long id ){
		tvShowRepository.deleteById(id);
	}

}
