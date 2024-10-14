package com.team1.dealerApp.rental;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

  @PostMapping()
  public ResponseEntity<?> addRental(@RequestParam (name = "userId") UUID userId, @RequestBody CreateRentalDTO createRentalDTO){
    try{
        return ResponseEntity.ok(rentalService.addRental(userId, createRentalDTO));
    } catch (BadRequestException e) {
        log.error("Error in adding a new Rental {} : {}", createRentalDTO, e.getMessage(), e.getStackTrace());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllRentalByUserId(@PathVariable ("userId") UUID userId, @RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size){
        try{
            return ResponseEntity.ok(rentalService.getAllRentalByUserId(userId, page, size));
        } catch (BadRequestException e) {
            log.error("Error in get all rental by user Id {} : {}", userId, e.getMessage(), e.getStackTrace());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PatchMapping("/{rentalId}")
    public ResponseEntity<?> updateRentalEndDate(@PathVariable ("rentalId") Long id, @RequestBody LocalDateTime dateTime){
        try{
            return ResponseEntity.ok(rentalService.updateRentalEndDate(id, dateTime));
        } catch (NoSuchElementException e){
            log.error("Error in update Rental's end date with id {} : {}", id, e.getMessage(), e.getStackTrace());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
