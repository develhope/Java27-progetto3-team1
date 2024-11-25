package com.team1.cineBox.rental;

import com.team1.cineBox.video.movie.MovieDTO;
import com.team1.cineBox.video.tvshow.TvShowDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private double rentalPrice;
    private List<MovieDTO> movies;
    private List<TvShowDTO> tvShows;
    private UUID userId;
    private RentalStatus rentalStatus;

}
