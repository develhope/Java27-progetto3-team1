package com.team1.dealerApp.rental;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalDTO {

    private List<Long> movies = new ArrayList<>();
    private List<Long> tvShows = new ArrayList<>();

}
