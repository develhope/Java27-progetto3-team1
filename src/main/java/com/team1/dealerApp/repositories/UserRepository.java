package com.team1.dealerApp.repositories;

import com.team1.dealerApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository< User, UUID > {}
