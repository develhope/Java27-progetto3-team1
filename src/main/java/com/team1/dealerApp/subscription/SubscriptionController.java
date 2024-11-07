package com.team1.dealerApp.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @DeleteMapping("/a/{subscriptionId}")
    public ResponseEntity<Boolean> deleteSubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.deleteSubscription(subscriptionId));
    }

    @GetMapping("/a/{subscriptionId}/info")
    public ResponseEntity<SubscriptionDTO> checkSubscription(@PathVariable Long subscriptionId) throws NoSuchElementException {
        return ResponseEntity.ok(subscriptionService.getSubscriptionDetails(subscriptionId));
    }

    @GetMapping("/a/all-subscription")
    public ResponseEntity< Page <SubscriptionDTO> > getAllSubscriptions(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(subscriptionService.getAllSubscription(page, size));
    }

    @GetMapping("/a/active-subscriptions")
    public ResponseEntity<Page<SubscriptionDTO>> getAllActiveSubscriptions(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(subscriptionService.getAllActiveSubscriptions(page, size));
    }

    @GetMapping("/a/subscriptions-by-type")
    public ResponseEntity<Page<SubscriptionDTO>> getAllSubscriptionsByType(@RequestParam SubscriptionType type, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(subscriptionService.getAlleSubscriptionsByType(type, page, size));
    }

    @PatchMapping("/a/{subscriptionId}")
    public ResponseEntity<SubscriptionDTO> updateSubscriptionEndDate(@PathVariable("subscriptionId") Long subscriptionId, LocalDate date) throws NoSuchElementException{
        return ResponseEntity.ok(subscriptionService.adminUpdateSubscriptionEndDate(subscriptionId, date));
    }

}
