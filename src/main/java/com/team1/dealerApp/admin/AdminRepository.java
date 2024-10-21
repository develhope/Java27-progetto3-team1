package com.team1.dealerApp.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    boolean existByEmail(String email);
    Optional<Admin> findByEmail(String email);

}