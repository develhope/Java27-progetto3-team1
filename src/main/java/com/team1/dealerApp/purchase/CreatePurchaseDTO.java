package com.team1.dealerApp.purchase;

import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePurchaseDTO {

    private List<Movie> movies;
    private List<TvShow> tvShows;

}
