package com.team1.dealerApp.user;

import com.team1.dealerApp.subscription.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final SubscriptionMapper subscriptionMapper;

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .subscriptions(subscriptionMapper.toDTOList(user.getSubscriptions()))
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
