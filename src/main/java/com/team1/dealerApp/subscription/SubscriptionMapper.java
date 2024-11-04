package com.team1.dealerApp.subscription;

import java.util.List;

public class SubscriptionMapper {

    public SubscriptionDTO toDTO(Subscription subscription){
        return SubscriptionDTO.builder()
                .price(subscription.getPrice())
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .subscriptionType(subscription.getSubscriptionType())
                .endDate(subscription.getEndDate())
                .build();
    }

    public Subscription toEntity(SubscriptionDTO subscriptionDTO){
        return Subscription.builder()
                .price(subscriptionDTO.getPrice())
                .status(subscriptionDTO.getStatus())
                .endDate(subscriptionDTO.getEndDate())
                .subscriptionType(subscriptionDTO.getSubscriptionType())
                .startDate(subscriptionDTO.getStartDate())
                .build();
    }

    public List<SubscriptionDTO> toDTOList(List<Subscription> subscriptions){
        return subscriptions.stream().map(this::toDTO).toList();
    }

    public List<Subscription> toEntityList(List<SubscriptionDTO> subscriptions){
        return subscriptions.stream().map(this::toEntity).toList();
    }
}
