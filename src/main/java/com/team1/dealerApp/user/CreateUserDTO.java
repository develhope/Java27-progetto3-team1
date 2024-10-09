package com.team1.dealerApp.user;

import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
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

    private SubsciprionStatus subsciprionStatus;

    private List< Movie > watchedMovies;

    private List< TvShow > watchedShows;

}