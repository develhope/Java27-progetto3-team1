package com.team1.dealerApp.video.tvshow;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

	public TvShowDTO addTvShow (TvShowDTO tvShowDTO) throws BadRequestException {
		if(!tvShowRepository.existsByTitleAndDirector(tvShowDTO.getTitle(), tvShowDTO.getDirector())){
			tvShowRepository.save(tvShowMapper.toTvShow(tvShowDTO));
			return tvShowDTO;
		}
		throw new BadRequestException("This Show already exists");
	}

	public TvShowDTO updateShow (TvShowDTO tvShowDTO, Long id) throws NoSuchElementException {
		if(tvShowRepository.existsById(id)) {
			TvShow found = tvShowMapper.toTvShow(tvShowDTO);
			found.setId(id);
			tvShowRepository.save(found);
			return tvShowMapper.toTvShowDTO(found);
		}
		throw new NoSuchElementException("There is no show with id: " + id);
	}

	public TvShowDTO updateShowField ( Long id, Object value, String field ) throws NoSuchElementException {
		TvShow tvShow = tvShowRepository
				.findById(id)
				.orElseThrow(() -> new NoSuchElementException("No show with id: " + id));
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

	public boolean deleteShowById ( Long id ){
		tvShowRepository.deleteById(id);
		return true;
	}

}
