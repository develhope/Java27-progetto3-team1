package com.team1.dealerApp.video;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Year;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class Video {

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @Column(nullable = false)
    private List<String> cast;

    @Column(nullable = false)
    private String director;

    @Column(name = "release_year", nullable = false)
    private Year releaseYear;

    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    @Column(name = "rental_price", nullable = false)
    private Double rentalPrice;

    @Column(nullable = false)
    private String plot;

    @Column
    private Float rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "video_status", nullable = false)
    private VideoStatus videoStatus;

    @Column(name = "order_count" )
    private int orderCount = 0;

    @Column(name = "video_profit")
    private Double videoProfit = 0.0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

}
