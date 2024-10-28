package com.team1.dealerApp.purchase;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePurchaseDTO {

    private List<Long> movies;
    private List<Long> tvShows;

}
