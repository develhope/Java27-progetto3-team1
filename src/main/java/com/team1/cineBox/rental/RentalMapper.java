package com.team1.cineBox.rental;

import com.team1.cineBox.video.movie.Movie;
import com.team1.cineBox.video.movie.MovieMapper;
import com.team1.cineBox.video.tvshow.TvShow;
import com.team1.cineBox.video.tvshow.TvShowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class RentalMapper {
    private final MovieMapper movieMapper;
    private final TvShowMapper tvShowMapper;

    public Rental toRental(CreateRentalDTO createRentalDTO, List<Movie> movieList, List<TvShow> tvShowsList) {
        return Rental.builder()
                .movies(movieList)
                .tvShows(tvShowsList)
                .build();
    }

    public RentalDTO toDTO(Rental rental) {
        return RentalDTO.builder()
                .id(rental.getId())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .rentalPrice(rental.getRentalPrice())
                .userId(rental.getRenter().getId())
                .movies(rental.getMovies().stream().map(movieMapper::toMovieDTO).toList())
                .tvShows(rental.getTvShows().stream().map(tvShowMapper::toTvShowDTO).toList())
                .rentalStatus(rental.getRentalStatus())
                .build();
    }

}
