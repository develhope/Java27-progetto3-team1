package com.team1.cineBox.user;

import com.team1.cineBox.subscription.SubscriptionDTO;
import com.team1.cineBox.video.movie.MovieDTO;
import com.team1.cineBox.video.tvshow.TvShowDTO;
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

    private List<MovieDTO> watchedMovies;

    private List<TvShowDTO> watchedShows;

    private List< MovieDTO > movieWishList;

    private List< TvShowDTO > showWishList;

    private Role role;

    private List<SubscriptionDTO> subscriptions;

}
