package com.team1.dealerApp.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByStatus(Boolean status);
    List<Subscription> findBySubscriptionType(SubscriptionType subscriptionType);


}
