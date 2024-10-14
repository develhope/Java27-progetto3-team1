package com.team1.dealerApp.rental;

import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserService userService;

    public RentalDTO addRental(UUID userId, CreateRentalDTO createRentalDTO) throws BadRequestException {
        if(createRentalDTO.getMovies().isEmpty() && createRentalDTO.getTvShows().isEmpty()){
            throw new BadRequestException("Both lists can not be empty");
        }
        Rental userRental = rentalMapper.toRental(createRentalDTO);
        Double movieRentalPrice = createRentalDTO.getMovies().stream().mapToDouble(Movie::getRentalPrice).sum();
        Double showRentalPrice = createRentalDTO.getTvShows().stream().mapToDouble(TvShow::getRentalPrice).sum();
        Double totalPrice = movieRentalPrice + showRentalPrice;
        userRental.setRentalPrice(totalPrice);
        userRental.setStartDate(LocalDateTime.now());
        userRental.setEndDate(LocalDateTime.now().plusDays(14));
        userRental.setRenter(userService.getUserById(userId));
        userRental.setPaid(true);
        rentalRepository.save(userRental);
        return rentalMapper.toDTO(userRental);

    }

}
