package com.team1.dealerApp.video.movie;


public class MovieMapper {

    public Movie toMovie(MovieDTO movieDTO){
        return Movie.builder()
                .videoStatus(movieDTO.getVideoStatus())
                .plot(movieDTO.getPlot())
                .cast(movieDTO.getCast())
                .genre(movieDTO.getGenre())
                .title(movieDTO.getTitle())
                .director(movieDTO.getDirector())
                .purchasePrice(movieDTO.getPurchasePrice())
                .rentalPrice(movieDTO.getRentalPrice())
                .releaseYear(movieDTO.getYear())
                .runningTime(movieDTO.getRunningTime())
                .rating(movieDTO.getRating())
                .build();
    }

    public MovieDTO toMovieDTO(Movie movie){
        return MovieDTO.builder()
                .videoStatus(movie.getVideoStatus())
                .cast(movie.getCast())
                .genre(movie.getGenre())
                .director(movie.getDirector())
                .plot(movie.getPlot())
                .rating(movie.getRating())
                .runningTime(movie.getRunningTime())
                .year(movie.getReleaseYear())
                .title(movie.getTitle())
                .purchasePrice(movie.getPurchasePrice())
                .rentalPrice(movie.getRentalPrice())
                .build();
    }
}
