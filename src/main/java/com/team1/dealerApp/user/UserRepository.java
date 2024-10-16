package com.team1.dealerApp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository< User, UUID > {
	User findUserByEmail( String email );

	boolean existsByEmail( String email );
}
