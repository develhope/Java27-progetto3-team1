package com.team1.dealerApp.mappers;

import com.team1.dealerApp.entities.User;
import com.team1.dealerApp.models.dtos.UserDTO;

public class UserMapper {
	 public User toUser ( UserDTO userDTO ){
		 return User.builder()
				 .firstName(userDTO.getFirstName())
				 .lastName(userDTO.getLastName())
				 .email(userDTO.getEmail())
				 .phoneNumber(userDTO.getPhoneNumber())
				 .subsciprionStatus(userDTO.getSubsciprionStatus())
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
				.subsciprionStatus(user.getSubsciprionStatus())
				.watchedMovies(user.getWatchedMovies())
				.watchedShows(user.getWatchedShows())
				.build();
	}

}
