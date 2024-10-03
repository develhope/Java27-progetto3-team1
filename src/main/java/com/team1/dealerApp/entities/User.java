package com.team1.dealerApp.entities;

import com.team1.dealerApp.models.SubsciprionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class User {

	@Id
	@GeneratedValue
	private UUID id;

	@Setter
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Setter
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Setter
	@Column(nullable = false)
	private String email;

	@Setter
	@Column(name = "phone_number", length = 10)
	private String phoneNumber;

	@Setter
	@Column(nullable = false)
	private String password;//ToDo: integrare Spring Security per non salvare password in chiaro

	@Setter
	@Column(name = "subscription_status")
	@Enumerated(EnumType.STRING)
	private SubsciprionStatus subsciprionStatus;

	@Setter
	@OneToMany()
	private List<Movie> watchedMovies;

	@Setter
	@OneToMany
	private List<TvShow> watchedShows;
}
