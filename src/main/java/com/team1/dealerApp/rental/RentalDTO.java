package com.team1.dealerApp.rental;

import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double rentalPrice;
    private List<Movie> movies;
    private List<TvShow> tvShows;
    private UUID userId;
    private RentalStatus rentalStatus;

}
