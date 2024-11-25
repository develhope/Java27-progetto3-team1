package com.team1.cineBox.video.tvshow;

import org.springframework.stereotype.Component;

@Component
public class TvShowMapper {

    public TvShow toTvShow(TvShowDTO tvShowDTO){
        return TvShow.builder()
                .episode(tvShowDTO.getEpisode())
                .season(tvShowDTO.getSeason())
                .genre(tvShowDTO.getGenre())
                .cast(tvShowDTO.getCast())
                .plot(tvShowDTO.getPlot())
                .director(tvShowDTO.getDirector())
                .rating(tvShowDTO.getRating())
                .title(tvShowDTO.getTitle())
                .purchasePrice(tvShowDTO.getPurchasePrice())
                .rentalPrice(tvShowDTO.getRentalPrice())
                .releaseYear(tvShowDTO.getReleaseYear())
                .videoStatus(tvShowDTO.getVideoStatus())
                .ageRating(tvShowDTO.getAgeRating())
                .build();
    }

    public TvShowDTO toTvShowDTO(TvShow tvShow){
        return TvShowDTO.builder()
                .episode(tvShow.getEpisode())
                .season(tvShow.getSeason())
                .genre(tvShow.getGenre())
                .cast(tvShow.getCast())
                .plot(tvShow.getPlot())
                .director(tvShow.getDirector())
                .rating(tvShow.getRating())
                .title(tvShow.getTitle())
                .purchasePrice(tvShow.getPurchasePrice())
                .rentalPrice(tvShow.getRentalPrice())
                .releaseYear(tvShow.getReleaseYear())
                .videoStatus(tvShow.getVideoStatus())
                .ageRating(tvShow.getAgeRating())
                .build();
    }

    public AdminTvShowDTO toAdminShowDTO(TvShow tvShow){
        return AdminTvShowDTO.builder()
                .id(tvShow.getId())
                .title(tvShow.getTitle())
                .director(tvShow.getDirector())
                .videoProfit(tvShow.getVideoProfit())
                .purchasePrice(tvShow.getPurchasePrice())
                .rentalPrice(tvShow.getRentalPrice())
                .orderCount(tvShow.getOrderCount())
                .build();
    }

    public TvShow toTvShow ( CreateShowDTO createShowDTO){
        return TvShow.builder()
                .episode(createShowDTO.getEpisode())
                .season(createShowDTO.getSeason())
                .genre(createShowDTO.getGenre())
                .cast(createShowDTO.getCast())
                .plot(createShowDTO.getPlot())
                .director(createShowDTO.getDirector())
                .rating(createShowDTO.getRating())
                .title(createShowDTO.getTitle())
                .purchasePrice(createShowDTO.getPurchasePrice())
                .rentalPrice(createShowDTO.getRentalPrice())
                .releaseYear(createShowDTO.getReleaseYear())
                .videoStatus(createShowDTO.getVideoStatus())
                .ageRating(createShowDTO.getAgeRating())
                .videoProfit(createShowDTO.getVideoProfit())
                .orderCount(createShowDTO.getOrderCount())
                .build();

    }
}
