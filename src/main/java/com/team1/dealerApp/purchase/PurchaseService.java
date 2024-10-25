package com.team1.dealerApp.purchase;

import com.team1.dealerApp.user.UserService;
import com.team1.dealerApp.video.movie.Movie;
import com.team1.dealerApp.video.tvshow.TvShow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final UserService userService;

    public PurchaseDTO addPurchase(UserDetails user, CreatePurchaseDTO createPurchaseDTO) throws BadRequestException {
        if (createPurchaseDTO.getMovies().isEmpty() && createPurchaseDTO.getTvShows().isEmpty()) {
            throw new BadRequestException("Both lists can not be empty");
        }

        Purchase purchaseUser = purchaseMapper.toPurchase(createPurchaseDTO);

        Double moviePurchasePrice = createPurchaseDTO.getMovies().stream().mapToDouble(Movie::getPurchasePrice).sum();
        Double tvShowPurchasePrice = createPurchaseDTO.getTvShows().stream().mapToDouble(TvShow::getPurchasePrice).sum();

        Double totalPurchasePrice = moviePurchasePrice + tvShowPurchasePrice;

        purchaseUser.setPurchasePrice(totalPurchasePrice);
        purchaseUser.setPurchaser(userService.getUserByEmail(user));

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

        Purchase purchaseSelected = purchaseMapper.toPurchase(createPurchaseDTO);
        purchaseSelected.setId(id);
        purchaseRepository.save(purchaseSelected);

        return purchaseMapper.toDTO(purchaseSelected);
    }


    public boolean deletePurchaseById(Long id) {
        purchaseRepository.deleteById(id);
        return true;
    }
}
