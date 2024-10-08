package com.team1.dealerApp.models.dtos;

import com.team1.dealerApp.models.SubsciprionStatus;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private SubsciprionStatus subsciprionStatus;

	private List<Movie> watchedMovies;

	private List<TvShow> watchedShows;

}
