package com.team1.dealerApp.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
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
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions(){
        return ResponseEntity.ok(subscriptionService.getAllSubscription());
    }

    @GetMapping("/a/active-subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getAllActiveSubscriptions(){
        return ResponseEntity.ok(subscriptionService.getAllActiveSubscriptions());
    }

    @GetMapping("/a/subscriptions-by-type")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptionsByType(@RequestParam SubscriptionType type){
        return ResponseEntity.ok(subscriptionService.getAlleSubscriptionsByType(type));
    }

    @PatchMapping("/a/{subscriptionId}")
    public ResponseEntity<SubscriptionDTO> updateSubscriptionEndDate(@PathVariable("subscriptionId") Long subscriptionId, LocalDate date) throws NoSuchElementException{
        return ResponseEntity.ok(subscriptionService.adminUpdateSubscriptionEndDate(subscriptionId, date));
    }

}
