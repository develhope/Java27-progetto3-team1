package com.team1.dealerApp.rental;

import com.team1.dealerApp.user.User;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private LocalDate startDate = LocalDate.now();

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate= LocalDate.now();

    @Column(name = "rental_price", nullable = false)
    private double rentalPrice;

    @Column
    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;

    @ManyToMany
    @JoinTable(
            name = "rental_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id")
    )
    private List<Movie> movies;

    @ManyToMany
    @JoinTable(
            name = "rental_show",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id")

    )
    private List<TvShow> tvShows;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User renter;

}
