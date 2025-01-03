package com.team1.cineBox.subscription;

import com.team1.cineBox.user.User;
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
    @Enumerated(EnumType.STRING)
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
