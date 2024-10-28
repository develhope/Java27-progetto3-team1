package com.team1.dealerApp.purchase;

import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.movie.MovieService;
import com.team1.dealerApp.video.tvshow.TvShow;
import com.team1.dealerApp.video.tvshow.TvShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final UserService userService;
    private final MovieService movieService;
    private final TvShowService tvShowService;




    public PurchaseDTO addPurchase(UserDetails user, CreatePurchaseDTO createPurchaseDTO) throws BadRequestException {
        double totalPurchasePrice = 0.0;
        List<Movie> movieList = new ArrayList<>();
        List<TvShow> tvShowList = new ArrayList<>();
        if (createPurchaseDTO.getMovies().isEmpty() && createPurchaseDTO.getTvShows().isEmpty()) {
            throw new BadRequestException("Both lists can not be empty");
        } else if (!createPurchaseDTO.getMovies().isEmpty()) {
            movieList = createPurchaseDTO.getMovies().stream().map(movieService::getMovieById).toList();
            totalPurchasePrice += movieList.stream().mapToDouble(Movie::getPurchasePrice).sum();
        } else if (!createPurchaseDTO.getTvShows().isEmpty()) {
            tvShowList = createPurchaseDTO.getTvShows().stream().map(tvShowService::getShowById).toList();
            totalPurchasePrice+= tvShowList.stream().mapToDouble(TvShow::getPurchasePrice).sum();
        }

        Purchase purchaseUser = purchaseMapper.toPurchase(createPurchaseDTO, movieList, tvShowList);
        purchaseUser.setOrderStatus(OrderStatus.PAID);
        purchaseUser.setPurchasePrice(totalPurchasePrice);
        purchaseUser.setPurchaser(userService.getUserByEmail(user));

        purchaseRepository.save(purchaseUser);

        return purchaseMapper.toDTO(purchaseUser);
    }


    public Page<PurchaseDTO> getPurchaseByUserId(UserDetails user, int page, int size) throws NoSuchElementException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Purchase> purchaseFind = purchaseRepository.findByPurchaser_Email(user.getUsername(), pageable);

        if (purchaseFind.isEmpty()) {
            throw new NoSuchElementException("User with email " + user.getUsername() + " has no purchases");
        }
        return purchaseFind.map(purchaseMapper::toDTO);
    }


    public PurchaseDTO updatePurchase(Long id, CreatePurchaseDTO createPurchaseDTO) throws NoSuchElementException {
        purchaseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Purchase whit " + id + " not found!"));
        List<Movie> purchasedMovies = movieService.getAllMoviesById(createPurchaseDTO.getMovies());
        List<TvShow> purchasedTvShow =tvShowService.getAllTvShowsById(createPurchaseDTO.getTvShows());
        Purchase purchaseSelected = purchaseMapper.toPurchase(createPurchaseDTO,purchasedMovies, purchasedTvShow );
        purchaseSelected.setId(id);
        purchaseRepository.save(purchaseSelected);

        return purchaseMapper.toDTO(purchaseSelected);
    }


    public boolean deletePurchaseById(Long id) {
        purchaseRepository.deleteById(id);
        return true;
    }
}
