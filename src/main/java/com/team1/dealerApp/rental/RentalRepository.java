package com.team1.dealerApp.rental;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("select r from Rental r where r.renter.id = :id")
    Page<Rental> findByRenterId(UUID id, Pageable pageable);

    Page<Rental> findByRenter_Email(String email, Pageable pageable);

    @Query("select r from Rental r where r.renter.email = ?1 and r.id = ?2")
    Optional<Rental> findByRenter_EmailAndId(String email, Long id);

}
