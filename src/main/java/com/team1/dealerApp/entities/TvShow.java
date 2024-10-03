package com.team1.dealerApp.entities;

import com.team1.dealerApp.models.Genre;
import com.team1.dealerApp.models.Video;
import com.team1.dealerApp.models.VideoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Year;
import java.util.List;

@Entity
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
public class TvShow extends Video {

    @Column(nullable = false)
    private int season;

    @Column(nullable = false)
    private int episode;


    public TvShow(Long id, String title, Genre genre, List<String> cast, String director, Year year, double purchasePrice, double rentalPrice, String plot, float rating, VideoStatus videoStatus, int season, int episode) {
        super(id, title, genre, cast, director, year, purchasePrice, rentalPrice, plot, rating, videoStatus);
        this.season = season;
        this.episode = episode;
    }
}
