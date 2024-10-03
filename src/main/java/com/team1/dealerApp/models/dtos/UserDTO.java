package com.team1.dealerApp.models.dtos;

import com.team1.dealerApp.models.SubsciprionStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {

	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private SubsciprionStatus subsciprionStatus;
	private List<Movie> watchedMovies;
	private List<TvShow> watchedShows;
}
