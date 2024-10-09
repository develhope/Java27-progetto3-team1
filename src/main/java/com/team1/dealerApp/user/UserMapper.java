package com.team1.dealerApp.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	 public User toUser ( UserDTO userDTO ){
		 return User.builder()
				 .firstName(userDTO.getFirstName())
				 .lastName(userDTO.getLastName())
				 .email(userDTO.getEmail())
				 .phoneNumber(userDTO.getPhoneNumber())
				 .subscriptionStatus(userDTO.getSubsciprionStatus())
				 .watchedMovies(userDTO.getWatchedMovies())
				 .watchedShows(userDTO.getWatchedShows())
				 .build();
	 }

	public UserDTO toUserDTO ( User user ){
		return UserDTO.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.phoneNumber(user.getPhoneNumber())
				.subsciprionStatus(user.getSubscriptionStatus())
				.watchedMovies(user.getWatchedMovies())
				.watchedShows(user.getWatchedShows())
				.build();
	}


	public User fromCreateUserDTOToUser ( CreateUserDTO createUserDTO ) {
		 return User.builder()
				 .firstName(createUserDTO.getFirstName())
				 .lastName(createUserDTO.getLastName())
				 .email(createUserDTO.getEmail())
				 .phoneNumber(createUserDTO.getPhoneNumber())
				 .subscriptionStatus(createUserDTO.getSubscriptionStatus())
				 .watchedMovies(createUserDTO.getWatchedMovies())
				 .watchedShows(createUserDTO.getWatchedShows())
				 .build();
	}

}
