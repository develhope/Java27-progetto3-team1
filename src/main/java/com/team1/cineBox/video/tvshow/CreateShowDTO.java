package com.team1.cineBox.video.tvshow;


import com.team1.cineBox.video.AgeRating;
import com.team1.cineBox.video.Genre;
import com.team1.cineBox.video.VideoStatus;
import lombok.*;

import java.time.Year;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateShowDTO {

	private String title;

	private Genre genre;

	private List <String> cast;

	private String director;

	private Year releaseYear;

	private Double purchasePrice;

	private Double rentalPrice;

	private String plot;

	private Float rating;

	private VideoStatus videoStatus;

	private int orderCount = 0;

	private Double videoProfit = 0.0;

	private AgeRating ageRating;

	private Integer season;

	private Integer episode;

}
