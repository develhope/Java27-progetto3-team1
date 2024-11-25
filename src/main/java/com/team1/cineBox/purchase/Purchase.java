package com.team1.cineBox.purchase;

import com.team1.cineBox.user.User;
import com.team1.cineBox.video.movie.Movie;
import com.team1.cineBox.video.tvshow.TvShow;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "purchase_price", nullable = false)
    private double purchasePrice;

    @ManyToMany
    @JoinTable(
            name = "purchased_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "purchase_id")
    )
    private List<Movie> movies;

    @ManyToMany
    @JoinTable(
            name = "purchased_show",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "purchase_id")

    )
    private List<TvShow> tvShows;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User purchaser;

}
