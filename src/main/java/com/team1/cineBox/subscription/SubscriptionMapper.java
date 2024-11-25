package com.team1.cineBox.subscription;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressWarnings("unused")
public class SubscriptionMapper {

    public SubscriptionDTO toDTO(Subscription subscription){
        return SubscriptionDTO.builder()
                .id(subscription.getId())
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
