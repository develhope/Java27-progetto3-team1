package com.team1.cineBox.user;

import com.team1.cineBox.video.movie.Movie;
import com.team1.cineBox.video.tvshow.TvShow;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private List<Movie> watchedMovies;

    private List<TvShow> watchedShows;

}
