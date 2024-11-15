package com.team1.dealerApp.video.tvshow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@SuppressWarnings( "unused" )
@Slf4j
@RestController
@RequiredArgsConstructor
public class TvShowController {

	private final TvShowService tvShowService;

	@PreAuthorize( "hasRole('USER')" )
	@GetMapping( "/u/tvShows" )
	public ResponseEntity < Page < TvShowDTO > > getAllShows(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) throws BadRequestException {
		return ResponseEntity.ok(tvShowService.getAllShows(page,size));
	}

	@PreAuthorize( "hasRole('USER')" )
	@GetMapping( "/u/tvShows/{id}" )
	public ResponseEntity < TvShowDTO > getShowById( @PathVariable Long id ) {
		TvShowDTO tvShowDTO = tvShowService.getShowDTOById(id);
		log.debug("Found user with id: {}", id);
		return ResponseEntity.ok(tvShowDTO);
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@PostMapping( "/a/tvShows" )
	public ResponseEntity < TvShowDTO > addTvShow( @RequestBody CreateShowDTO tvShowDTO ) throws BadRequestException {
		TvShowDTO added = tvShowService.addTvShow(tvShowDTO);
		log.debug("TvShow added successfully in db");
		return ResponseEntity.status(HttpStatus.CREATED).body(added);
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@PutMapping( "/a/tvShows/{id}" )
	public ResponseEntity < TvShowDTO > updateShow( @RequestBody CreateShowDTO tvShowDTO, @PathVariable Long id ) throws NoSuchElementException {
		TvShowDTO updated = tvShowService.updateShow(tvShowDTO, id);
		log.debug("Show updated successfully");
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@PatchMapping( "/a/tvShows/{id}" )
	public ResponseEntity < TvShowDTO > updateShowField( @PathVariable Long id, @RequestBody Map <String, Object> fieldValueMap ) throws NoSuchElementException, NoSuchFieldException, IllegalAccessException {
		return ResponseEntity.ok(tvShowService.updateShowField(id, fieldValueMap));
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@DeleteMapping( "/a/tvShows/{id}" )
	public ResponseEntity < Boolean > deleteShowById( @PathVariable Long id ) {
		return ResponseEntity.ok(tvShowService.deleteShowById(id));
	}

	@GetMapping("/a/tvShows/sales/{id}")
	public ResponseEntity<AdminTvShowDTO> getSalesById(@PathVariable("id") Long id) throws NoSuchElementException{
		return ResponseEntity.ok(tvShowService.getSalesById(id));
	}

	@GetMapping("/a/tvShows/sales")
	public ResponseEntity<Page<AdminTvShowDTO>> getSales(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
		return ResponseEntity.ok(tvShowService.getSales(page, size));
	}
}
