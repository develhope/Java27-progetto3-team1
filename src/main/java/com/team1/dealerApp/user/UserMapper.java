package com.team1.dealerApp.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .watchedMovies(userDTO.getWatchedMovies())
                .watchedShows(userDTO.getWatchedShows())
                .build();
    }

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .watchedMovies(user.getWatchedMovies())
                .watchedShows(user.getWatchedShows())
                .build();
    }


    public User toUser(CreateUserDTO createUserDTO) {
        return User.builder()
                .firstName(createUserDTO.getFirstName())
                .lastName(createUserDTO.getLastName())
                .email(createUserDTO.getEmail())
                .phoneNumber(createUserDTO.getPhoneNumber())
                .watchedMovies(createUserDTO.getWatchedMovies())
                .watchedShows(createUserDTO.getWatchedShows())
                .password(createUserDTO.getPassword())
                .build();
    }

}
