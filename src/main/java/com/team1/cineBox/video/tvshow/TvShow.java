package com.team1.cineBox.video.tvshow;

import com.team1.cineBox.rental.Rental;
import com.team1.cineBox.video.AgeRating;
import com.team1.cineBox.video.Genre;
import com.team1.cineBox.video.Video;
import com.team1.cineBox.video.VideoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Year;
import java.util.List;

@Entity
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
public class TvShow extends Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer season;

    @Column(nullable = false)
    private Integer episode;

    @ManyToMany(mappedBy = "tvShows")
    private List<Rental> rentals;

    public TvShow(String title, Genre genre, List<String> cast, String director, Year releaseYear, Double purchasePrice, Double rentalPrice, String plot, Float rating, VideoStatus videoStatus, int orderCount, Double videoProfit, AgeRating ageRating, Integer season, Integer episode) {
        super(title, genre, cast, director, releaseYear, purchasePrice, rentalPrice, plot, rating, videoStatus, orderCount, videoProfit, ageRating);
        this.season = season;
        this.episode = episode;
    }
}
