package com.team1.dealerApp.video.tvshow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequestMapping("/tvshow")
@RequiredArgsConstructor
public class TvShowController {

	private final TvShowService tvShowService;

	@GetMapping
	public ResponseEntity<List<TvShowDTO>> getAllShows () throws BadRequestException{
		return ResponseEntity.ok(tvShowService.getAllShows());
	}

	@GetMapping("{id}")
	public ResponseEntity<TvShowDTO> getShowById ( @PathVariable Long id ) throws BadRequestException{
			TvShowDTO tvShowDTO = tvShowService.getShowById(id);
			log.debug("Found user with id: {}", id);
			return ResponseEntity.ok(tvShowDTO);
	}

	@PostMapping()
	public ResponseEntity<TvShowDTO> addTvShow (@RequestBody TvShowDTO tvShowDTO) throws BadRequestException {
			TvShowDTO added = tvShowService.addTvShow(tvShowDTO);
			log.debug("TvShow added successfully in db");
			return ResponseEntity.status(HttpStatus.CREATED).body(added);
	}

	@PutMapping("{id}")
	public ResponseEntity<TvShowDTO> updateShow (@RequestBody TvShowDTO tvShowDTO, @PathVariable Long id) throws NoSuchElementException{
			TvShowDTO updated = tvShowService.updateShow(tvShowDTO, id);
			log.debug("Show updated successfully");
			return ResponseEntity.ok(updated);
	}

	@PatchMapping("{id}")
	public ResponseEntity<TvShowDTO> updateShowField (@PathVariable Long id, @RequestParam(name = "field") String fieldName, @RequestBody Object value) throws NoSuchElementException{
			return ResponseEntity.ok(tvShowService.updateShowField(id,value, fieldName));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Boolean> deleteShowById (@PathVariable Long id){
		return ResponseEntity.ok(tvShowService.deleteShowById(id));
	}

}
