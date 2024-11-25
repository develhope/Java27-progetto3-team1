package com.team1.cineBox.purchase;

import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/u/purchases")
    public ResponseEntity<String> addPurchase( @AuthenticationPrincipal UserDetails user, @RequestBody CreatePurchaseDTO createPurchaseDTO) throws BadRequestException, PayPalRESTException {
        
        return ResponseEntity.ok(purchaseService.addPurchase(user, createPurchaseDTO));

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/u/purchases")
    public ResponseEntity<Page<PurchaseDTO>> getUserPurchase(@AuthenticationPrincipal UserDetails user, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws NoSuchElementException {
        return ResponseEntity.ok(purchaseService.getPurchaseByUserId(user, page, size));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/a/purchases/{purchaseId}")
    public ResponseEntity<?> updatePurchase(@PathVariable("purchaseId") Long id, @RequestBody CreatePurchaseDTO createPurchaseDTO) throws NoSuchElementException {
        return ResponseEntity.ok(purchaseService.updatePurchase(id, createPurchaseDTO));

    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/u/purchases/{purchaseId}")
    public ResponseEntity<?> deletePurchaseById(@PathVariable("purchaseId") Long id) {
        purchaseService.deletePurchaseById(id);
        return ResponseEntity.ok("Purchase deleted");
    }
}
