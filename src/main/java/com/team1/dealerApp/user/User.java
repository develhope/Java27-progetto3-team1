package com.team1.dealerApp.user;

import com.team1.dealerApp.rental.Rental;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Setter
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
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

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

	@Override
	public Collection < ? extends GrantedAuthority > getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return email;
	}

}
