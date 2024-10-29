package com.team1.dealerApp.video.tvshow;

import lombok.*;


@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class AdminTvShowDTO {
    private Long id;
    private String title;
    private String director;
    private Double videoProfit;
    private int orderCount;
    private Double purchasePrice;
    private Double rentalPrice;
}
