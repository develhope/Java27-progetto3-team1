package com.team1.dealerApp.video.tvshow;

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
                .build();

    }
}
