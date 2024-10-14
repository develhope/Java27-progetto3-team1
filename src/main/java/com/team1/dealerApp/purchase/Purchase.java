package com.team1.dealerApp.purchase;

import com.team1.dealerApp.user.SubscriptionStatus;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
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
    @Column(name = "subscription_status",nullable = false)
    private SubscriptionStatus subscriptionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status",nullable = false)
    private OrderStatus orderStatus;

    @Column //(nullable = false) ???
    private boolean paid;

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
    @Column(nullable = false)
    private User purchaser;

}
