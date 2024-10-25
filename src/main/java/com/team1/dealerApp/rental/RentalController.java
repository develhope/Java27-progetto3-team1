package com.team1.dealerApp.rental;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

  @PostMapping()
  public ResponseEntity<RentalDTO> addRental(@AuthenticationPrincipal UserDetails user, @RequestBody CreateRentalDTO createRentalDTO) throws BadRequestException{
        return ResponseEntity.ok(rentalService.addRental(user, createRentalDTO));
  }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<RentalDTO>> getActiveUserRentals(@AuthenticationPrincipal UserDetails user, @RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) throws NoSuchElementException{
            return ResponseEntity.ok(rentalService.getActiveUserRentals(user, page, size));
    }

    @PatchMapping("/{rentalId}")
    public ResponseEntity<RentalDTO> updateRentalEndDate(@AuthenticationPrincipal UserDetails user, @PathVariable ("rentalId") Long id, @RequestBody LocalDateTime dateTime) throws NoSuchElementException{
            return ResponseEntity.ok(rentalService.updateRentalEndDate(user, id, dateTime));
    }

    @DeleteMapping("/{rentalId}")
    public ResponseEntity<Boolean> deleteRental(@PathVariable ("rentalId") Long id){
        return ResponseEntity.ok(rentalService.deleteRental(id));
    }

}
