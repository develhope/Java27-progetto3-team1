package com.team1.dealerApp.controllers;

import com.team1.dealerApp.models.dtos.TvShowDTO;
import com.team1.dealerApp.services.TvShowService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<?> addTvShow (@RequestBody TvShowDTO tvShowDTO){
		return ResponseEntity.ok(tvShowService.addTvShow(tvShowDTO));
	}



}
