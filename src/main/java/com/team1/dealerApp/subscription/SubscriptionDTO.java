package com.team1.dealerApp.subscription;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {

    private Double price;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean status;

}
