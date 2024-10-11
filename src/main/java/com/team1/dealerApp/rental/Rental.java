package com.team1.dealerApp.rental;

import com.team1.dealerApp.user.User;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "rental_price", nullable = false)
    private double rentalPrice;

    @Column
    private boolean paid;

    @Column(name = "rental_movies")
    private List<Movie> rentalMovies;

    @Column(name = "rental_tv_shows")
    private List<TvShow> rentalTvShows;

    @ManyToOne
    @Column(nullable = false)
    private User renter;


}
