package com.team1.dealerApp.rental;

import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    public Rental toRental(CreateRentalDTO createRentalDTO){
        return Rental.builder()
                .movies(createRentalDTO.getMovies())
                .tvShows(createRentalDTO.getTvShows())
                .build();
    }

    public RentalDTO toDTO(Rental rental){
        return RentalDTO.builder()
                .id(rental.getId())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .paid(rental.isPaid())
                .rentalPrice(rental.getRentalPrice())
                .userId(rental.getRenter().getId())
                .movies(rental.getMovies())
                .tvShows(rental.getTvShows())
                .build();
    }
}
