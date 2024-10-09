package com.team1.dealerApp.video.movie;

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
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class Movie extends Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="running_time", nullable = false)
    private Integer runningTime;

    public Movie(String title, Genre genre, List<String> cast, String director, Year releaseYear, double purchasePrice, double rentalPrice, String plot, float rating, VideoStatus videoStatus, int runningTime) {
        super(title, genre, cast, director, releaseYear, purchasePrice, rentalPrice, plot, rating, videoStatus);
        this.runningTime = runningTime;
    }
}
