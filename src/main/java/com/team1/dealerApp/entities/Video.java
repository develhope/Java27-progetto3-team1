package com.team1.dealerApp.entities;

import com.team1.dealerApp.models.Genre;
import com.team1.dealerApp.models.VideoStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(name="running_time", nullable = false)
    private int runningTime;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @Setter
    @Column(nullable = false)
    private List<String> cast;

    @Setter
    @Column(nullable = false)
    private String director;

    @Setter
    @Column(nullable = false)
    private Year year;

    @Setter
    @Column(name = "purchase_price", nullable = false)
    private double purchasePrice;

    @Setter
    @Column(name = "rental_price", nullable = false)
    private double rentalPrice;

    @Setter
    @Column(nullable = false)
    private String plot;

    @Setter
    @Column
    private float rating;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "video_status", nullable = false)
    private VideoStatus videoStatus;



}
