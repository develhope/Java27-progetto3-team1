package com.team1.dealerApp.user;

import com.team1.dealerApp.video.movie.MovieDTO;
import com.team1.dealerApp.video.tvshow.TvShowDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WatchedVideosDTO {

	List< MovieDTO > watchedMovies = new ArrayList <>();
	List< TvShowDTO > watchedShows = new ArrayList<>();

}
