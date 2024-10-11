package com.team1.dealerApp.video.tvshow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequestMapping("/tvshow")
@RequiredArgsConstructor
public class TvShowController {

	private final TvShowService tvShowService;

	@GetMapping
	public ResponseEntity<?> getAllUsers (){
		try {
			return ResponseEntity.ok(tvShowService.getAllShows());
		}catch ( BadRequestException e ){
			log.error("Error in getting users: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getShowById ( @PathVariable Long id ){
		try {
			TvShowDTO tvShowDTO = tvShowService.getShowById(id);
			log.debug("Found user with id: {}", id);
			return ResponseEntity.ok(tvShowDTO);
		}catch ( BadRequestException e ){
			log.error("Error in getting user: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping()
	public ResponseEntity<?> addTvShow (@RequestBody TvShowDTO tvShowDTO) {
		try {
			TvShowDTO added = tvShowService.addTvShow(tvShowDTO);
			log.debug("TvShow added successfully in db");
			return ResponseEntity.status(HttpStatus.CREATED).body(added);
		} catch ( BadRequestException e ) {
			log.error("Error in adding tvShow: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateShow (@RequestBody TvShowDTO tvShowDTO, @PathVariable Long id){
		try {
			TvShowDTO updated = tvShowService.updateShow(tvShowDTO, id);
			log.debug("Show updated successfully");
			return ResponseEntity.ok(updated);
		}catch ( NoSuchElementException e ){
			log.error("Error in updating show with id {}: {}",id, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PatchMapping("{id}")
	public ResponseEntity<?> updateShowField (@PathVariable Long id, @RequestParam(name = "field") String fieldName, @RequestBody Object value){
		try{
			return ResponseEntity.ok(tvShowService.updateShowField(id,value, fieldName));
		} catch ( NoSuchElementException e ) {
			log.error("Error in updating product with id {}: {}", id, e.getMessage());
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteShowById (@PathVariable Long id){
		tvShowService.deleteShowById(id);
		return ResponseEntity.ok().build();
	}

}
