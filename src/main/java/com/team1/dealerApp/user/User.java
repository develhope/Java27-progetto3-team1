package com.team1.dealerApp.user;

import com.team1.dealerApp.rental.Rental;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
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
	@Column(nullable = false, unique = true)
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
	private SubscriptionStatus subscriptionStatus;

	@Setter
	@OneToMany()
	private List< Movie > watchedMovies;

	@Setter
	@OneToMany
	private List< TvShow > watchedShows;

	@Setter
	@OneToMany(mappedBy = "renter")
	private List<Rental> rentals;

	public void setId(UUID id) {
	}

}
