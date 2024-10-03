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
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class Movie extends Video {

    @Column(name="running_time", nullable = false)
    private int runningTime;

    public Movie(Long id, String title, Genre genre, List<String> cast, String director, Year releaseYear, double purchasePrice, double rentalPrice, String plot, float rating, VideoStatus videoStatus, int runningTime) {
        super(id, title, genre, cast, director, releaseYear, purchasePrice, rentalPrice, plot, rating, videoStatus);
        this.runningTime = runningTime;
    }
}
