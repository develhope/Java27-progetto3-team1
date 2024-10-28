package com.team1.dealerApp.purchase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Page<Purchase> findByPurchaserId(UUID userId, Pageable pageable);

    Page<Purchase> findByPurchaser_Email(String email, Pageable pageable);

}
