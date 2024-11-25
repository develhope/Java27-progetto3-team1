package com.team1.cineBox.purchase;

import com.team1.cineBox.video.movie.MovieDTO;
import com.team1.cineBox.video.tvshow.TvShowDTO;
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

    private List<MovieDTO> movies;
    private List<TvShowDTO> tvShows;

    private UUID userId;
}
