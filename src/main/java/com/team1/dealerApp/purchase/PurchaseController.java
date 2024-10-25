package com.team1.dealerApp.purchase;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping()
    public ResponseEntity<PurchaseDTO> addPurchase(@AuthenticationPrincipal UserDetails user, @RequestBody CreatePurchaseDTO createPurchaseDTO) throws BadRequestException {
        return ResponseEntity.ok(purchaseService.addPurchase(user, createPurchaseDTO));

    }


    @GetMapping("/{userId}")
    public ResponseEntity<Page<PurchaseDTO>> getUserPurchase(@AuthenticationPrincipal UserDetails user, @RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) throws NoSuchElementException{
        return ResponseEntity.ok(purchaseService.getPurchaseByUserId(user, page, size));

    }


    @PutMapping("/{purchaseId}")
    public ResponseEntity<?> updatePurchase(@PathVariable ("purchaseId") Long id, @RequestBody CreatePurchaseDTO createPurchaseDTO) throws NoSuchElementException {
        return ResponseEntity.ok(purchaseService.updatePurchase(id, createPurchaseDTO));

    }


    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<?> deletePurchaseById(@PathVariable("purchaseId") Long id) {
        purchaseService.deletePurchaseById(id);
        return ResponseEntity.ok("Purchase deleted");
    }


}
