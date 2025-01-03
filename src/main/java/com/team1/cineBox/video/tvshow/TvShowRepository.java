package com.team1.cineBox.video.tvshow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Long> {

	boolean existsByTitleAndDirector( String title, String director );
}
