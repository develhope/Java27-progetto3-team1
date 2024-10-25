package com.team1.dealerApp.purchase;

import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.movie.MovieMapper;
import com.team1.dealerApp.video.tvshow.TvShow;
import com.team1.dealerApp.video.tvshow.TvShowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class PurchaseMapper {
    private final MovieMapper movieMapper;
    private final TvShowMapper tvShowMapper;

    public Purchase toPurchase(CreatePurchaseDTO createPurchaseDTO, List<Movie> movieList, List<TvShow> tvShowsList) {
        return Purchase.builder()
                .movies(movieList)
                .tvShows(tvShowsList)
                .build();
    }

    public PurchaseDTO toDTO(Purchase purchase) {
        return PurchaseDTO.builder()
                .id(purchase.getId())
                .purchasePrice(purchase.getPurchasePrice())
                .movies(purchase.getMovies().stream().map(movieMapper::toMovieDTO).toList())
                .tvShows(purchase.getTvShows().stream().map(tvShowMapper::toTvShowDTO).toList())
                .userId(purchase.getPurchaser().getId())
                .orderStatus(purchase.getOrderStatus())
                .build();
    }
}
