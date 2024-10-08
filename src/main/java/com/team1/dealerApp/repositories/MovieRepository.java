package com.team1.dealerApp.repositories;

import com.team1.dealerApp.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
