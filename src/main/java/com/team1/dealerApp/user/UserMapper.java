package com.team1.dealerApp.user;

import com.team1.dealerApp.subscription.SubscriptionMapper;
import com.team1.dealerApp.video.movie.MovieMapper;
import com.team1.dealerApp.video.tvshow.TvShowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final SubscriptionMapper subscriptionMapper;
    private final MovieMapper movieMapper;
    private final TvShowMapper tvShowMapper;

    public UserDTO toUserDTO( User user ) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .subscriptions(subscriptionMapper.toDTOList(user.getSubscriptions()))
                .watchedMovies(user.getWatchedMovies().stream().map(movieMapper::toMovieDTO).toList())
                .watchedShows(user.getWatchedShows().stream().map(tvShowMapper::toTvShowDTO).toList())
                .movieWishList(user.getMovieWishList().stream().map(movieMapper::toMovieDTO).toList())
                .showWishList(user.getShowWishList().stream().map(tvShowMapper::toTvShowDTO).toList())
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
