package com.team1.dealerApp.models.dtos;

import com.team1.dealerApp.models.Genre;
import com.team1.dealerApp.models.VideoStatus;
import lombok.*;

import java.time.Year;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TvShowDTO {

    private String title;

    private Genre genre;

    private List<String> cast;

    private String director;

    private Year releaseYear;

    private double purchasePrice;

    private double rentalPrice;

    private String plot;

    private float rating;

    private int season;

    private int episode;

    private VideoStatus videoStatus;

}
