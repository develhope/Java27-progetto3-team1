package com.team1.dealerApp.subscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	Page < Subscription > findByStatusOrderByStatusAsc( Boolean status, Pageable pageable );

	Page < Subscription > findBySubscriptionType( SubscriptionType subscriptionType, Pageable pageable );

}
