package com.team1.dealerApp.video.tvshow;

import com.team1.dealerApp.rental.Rental;
import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.Video;
import com.team1.dealerApp.video.VideoStatus;
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


    public TvShow( String title, Genre genre, List<String> cast, String director, Year year, double purchasePrice, double rentalPrice, String plot, float rating, VideoStatus videoStatus, int season, int episode) {
        super(title, genre, cast, director, year, purchasePrice, rentalPrice, plot, rating, videoStatus);
        this.season = season;
        this.episode = episode;
    }
}
