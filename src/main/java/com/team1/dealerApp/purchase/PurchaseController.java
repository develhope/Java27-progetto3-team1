package com.team1.dealerApp.purchase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping()
    public ResponseEntity<?> addPurchase(@RequestParam(name = "userId") UUID userId, @RequestBody CreatePurchaseDTO createPurchaseDTO) {
        try {
            return ResponseEntity.ok(purchaseService.addPurchase(userId, createPurchaseDTO));
        } catch (BadRequestException e) {
            log.error("Error in adding a new Purchase {} : {}", createPurchaseDTO, e.getMessage(), e.getStackTrace());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getPurchaseByUserId(@PathVariable ("userId") UUID userId, @RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) {
        try{
            return ResponseEntity.ok(purchaseService.getPurchaseByUserId(userId, page, size));
        } catch (NoSuchElementException e){
            log.error("Error in get purchase by UserId {} : {}", userId, e.getMessage(), e.getStackTrace());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/{purchaseId}")
    public ResponseEntity<?> updatePurchase(@PathVariable ("purchaseId") Long id, @RequestBody CreatePurchaseDTO createPurchaseDTO) {
        try {
            return ResponseEntity.ok(purchaseService.updatePurchase(id, createPurchaseDTO));
        } catch (NoSuchElementException e){
            log.error("Error in update Purchase with id {} : {}", id, e.getMessage(), e.getStackTrace());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<?> deletePurchaseById(@PathVariable("purchaseId") Long id) {
        purchaseService.deletePurchaseById(id);
        return ResponseEntity.ok("Purchase deleted");
    }


}
