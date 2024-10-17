package com.team1.dealerApp.purchase;

import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {

    private Long id;
    private OrderStatus orderStatus;
    private Double purchasePrice;

    private List<Movie> movies;
    private List<TvShow> tvShows;

    private UUID userId;
}
