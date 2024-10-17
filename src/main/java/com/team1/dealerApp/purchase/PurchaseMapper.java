package com.team1.dealerApp.purchase;

public class PurchaseMapper {

    public Purchase toPurchase(CreatePurchaseDTO createPurchaseDTO) {
        return Purchase.builder()
                .movies(createPurchaseDTO.getMovies())
                .tvShows(createPurchaseDTO.getTvShows())
                .build();
    }

    public PurchaseDTO toDTO(Purchase purchase) {
        return PurchaseDTO.builder()
                .id(purchase.getId())
                .purchasePrice(purchase.getPurchasePrice())
                .movies(purchase.getMovies())
                .tvShows(purchase.getTvShows())
                .userId(purchase.getPurchaser().getId())
                .build();
    }
}
