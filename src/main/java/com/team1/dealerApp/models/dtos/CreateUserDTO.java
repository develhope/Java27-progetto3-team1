package com.team1.dealerApp.models.dtos;

import com.team1.dealerApp.models.SubsciprionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CreateUserDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private SubsciprionStatus subsciprionStatus;

    private List<Movie> watchedMovies;

    private List<TvShow> watchedShows;
}
