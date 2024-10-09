package com.team1.dealerApp.video.movie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieByTitleAndDirector(String title, String director);
}
