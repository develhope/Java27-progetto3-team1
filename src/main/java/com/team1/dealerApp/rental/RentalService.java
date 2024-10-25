package com.team1.dealerApp.rental;

import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
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

    public RentalDTO addRental(UserDetails user, CreateRentalDTO createRentalDTO) throws BadRequestException {
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
        userRental.setRenter(userService.getUserByEmail(user));
        userRental.setRentalStatus(RentalStatus.ACTIVE);
        rentalRepository.save(userRental);
        return rentalMapper.toDTO(userRental);

    }

    public Page<RentalDTO> getActiveUserRentals(UserDetails user, int page, int size) throws NoSuchElementException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Rental> rentalsFind = rentalRepository.findByRenter_Email(user.getUsername(), pageable);
        if(rentalsFind.isEmpty()){
            throw new NoSuchElementException("Rental's list is empty");
        }
        return rentalsFind.map(rentalMapper::toDTO);
    }
    public RentalDTO updateRentalEndDate(UserDetails user, Long id, LocalDateTime dateTime) {
        Rental rentalFound = rentalRepository.findByRenter_EmailAndId(user.getUsername(), id).orElseThrow(()-> new NoSuchElementException("There is no rental with id " + id));
        rentalFound.setEndDate(dateTime);
        rentalRepository.save(rentalFound);
        return rentalMapper.toDTO(rentalFound);
    }

    public boolean deleteRental(Long id) {
        rentalRepository.deleteById(id);
        return true;
    }
}
