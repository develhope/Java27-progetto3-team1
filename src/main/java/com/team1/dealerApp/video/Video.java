package com.team1.dealerApp.video;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Year;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class Video {

    @Setter
    @Column(nullable = false)
    private String title;

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
    @Column(name = "release_year", nullable = false)
    private Year releaseYear;

    @Setter
    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    @Setter
    @Column(name = "rental_price", nullable = false)
    private Double rentalPrice;

    @Setter
    @Column(nullable = false)
    private String plot;

    @Setter
    @Column
    private Float rating;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "video_status", nullable = false)
    private VideoStatus videoStatus;



}
