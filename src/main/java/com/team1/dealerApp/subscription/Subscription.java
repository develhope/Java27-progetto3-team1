package com.team1.dealerApp.subscription;

import com.team1.dealerApp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( name = "subscription_type", nullable = false)
    @Enumerated
    private SubscriptionType subscriptionType;

    @Column(nullable = false)
    private Double price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private Boolean status = true;

    @ManyToOne()
    private User users;

}
