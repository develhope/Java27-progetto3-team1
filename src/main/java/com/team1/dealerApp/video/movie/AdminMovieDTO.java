package com.team1.dealerApp.video.movie;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class AdminMovieDTO  {

    private Long id;
    private String title;
    private String director;
    private Double videoProfit;
    private int orderCount;
    private Double purchasePrice;
    private Double rentalPrice;


}
