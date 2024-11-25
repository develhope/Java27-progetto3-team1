package com.team1.cineBox.subscription;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {

    private Long id;

    private SubscriptionType subscriptionType;

    private Double price;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean status;

}
