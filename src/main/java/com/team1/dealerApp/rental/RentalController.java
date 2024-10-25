package com.team1.dealerApp.rental;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class RentalController {

	private final RentalService rentalService;

	@PreAuthorize( "hasRole('USER')" )
	@PostMapping( "/u/rentals" )
	public ResponseEntity < RentalDTO > addRental( @RequestParam( name = "userId" ) UUID userId, @RequestBody CreateRentalDTO createRentalDTO ) throws BadRequestException {
		return ResponseEntity.ok(rentalService.addRental(userId, createRentalDTO));
	}

	@PreAuthorize( "hasRole('USER')" )
	@GetMapping( "/u/rentals/{userId}" )
	public ResponseEntity < Page < RentalDTO > > getRentalByUserId( @PathVariable( "userId" ) UUID userId, @RequestParam( defaultValue = "0" ) int page, @RequestParam( defaultValue = "10" ) int size ) throws NoSuchElementException {
		return ResponseEntity.ok(rentalService.getAllRentalByUserId(userId, page, size));
	}

	@PreAuthorize( "hasRole('USER')" )
	@PatchMapping( "/u/rentals/{rentalId}" )
	public ResponseEntity < RentalDTO > updateRentalEndDate( @PathVariable( "rentalId" ) Long id, @RequestBody LocalDateTime dateTime ) throws NoSuchElementException {
		return ResponseEntity.ok(rentalService.updateRentalEndDate(id, dateTime));
	}

	@PreAuthorize( "hasRole('USER')" )
	@DeleteMapping( "/u/rentals/{rentalId}" )
	public ResponseEntity < Boolean > deleteRental( @PathVariable( "rentalId" ) Long id ) {
		return ResponseEntity.ok(rentalService.deleteRental(id));
	}

}
