package com.team1.dealerApp.video.tvshow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings( "unused" )
@Slf4j
@RestController
@RequiredArgsConstructor
public class TvShowController {

	private final TvShowService tvShowService;

	@PreAuthorize( "hasRole('USER')" )
	@GetMapping( "/u/tvShows" )
	public ResponseEntity < List < TvShowDTO > > getAllShows() throws BadRequestException {
		return ResponseEntity.ok(tvShowService.getAllShows());
	}

	@PreAuthorize( "hasRole('USER')" )
	@GetMapping( "/u/tvShows/{id}" )
	public ResponseEntity < TvShowDTO > getShowById( @PathVariable Long id ) throws BadRequestException {
		TvShowDTO tvShowDTO = tvShowService.getShowById(id);
		log.debug("Found user with id: {}", id);
		return ResponseEntity.ok(tvShowDTO);
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@PostMapping( "/a/tvShows" )
	public ResponseEntity < TvShowDTO > addTvShow( @RequestBody TvShowDTO tvShowDTO ) throws BadRequestException {
		TvShowDTO added = tvShowService.addTvShow(tvShowDTO);
		log.debug("TvShow added successfully in db");
		return ResponseEntity.status(HttpStatus.CREATED).body(added);
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@PutMapping( "/a/tvShows/{id}" )
	public ResponseEntity < TvShowDTO > updateShow( @RequestBody TvShowDTO tvShowDTO, @PathVariable Long id ) throws NoSuchElementException {
		TvShowDTO updated = tvShowService.updateShow(tvShowDTO, id);
		log.debug("Show updated successfully");
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@PatchMapping( "/a/tvShows/{id}" )
	public ResponseEntity < TvShowDTO > updateShowField( @PathVariable Long id, @RequestParam( name = "field" ) String fieldName, @RequestBody Object value ) throws NoSuchElementException {
		return ResponseEntity.ok(tvShowService.updateShowField(id, value, fieldName));
	}

	@PreAuthorize( "hasRole('ADMIN')" )
	@DeleteMapping( "/a/tvShows/{id}" )
	public ResponseEntity < Boolean > deleteShowById( @PathVariable Long id ) {
		return ResponseEntity.ok(tvShowService.deleteShowById(id));
	}
}
