package com.team1.dealerApp.models.dtos;

import com.team1.dealerApp.models.Genre;
import com.team1.dealerApp.models.VideoStatus;

import lombok.*;

import java.time.Year;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {

    private String title;

    private int runningTime;

    private Genre genre;

    private List<String> cast;

    private String director;

    private Year year;

    private double purchasePrice;

    private double rentalPrice;

    private String plot;

    private float rating;

    private VideoStatus videoStatus;

}
