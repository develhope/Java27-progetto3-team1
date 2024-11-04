package com.team1.dealerApp.subscription;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public void updateSubscriptionEndDate(Long subscriptionId, LocalDate newEndDate) throws NoSuchElementException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(()-> new NoSuchElementException("There is no subscription with id " + subscriptionId));
        subscription.setEndDate(newEndDate);
        subscriptionRepository.save(subscription);
    }

    public boolean deleteSubscription(Long subscriptionId) {
        Boolean isActive = false;
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(()-> new NoSuchElementException("There is no subscription with id " + subscriptionId));
        subscription.setStatus(isActive);
        subscriptionRepository.save(subscription);
        return true;
    }

    public SubscriptionDTO getSubscriptionDetails(Long subscriptionId) throws NoSuchElementException {
        Subscription subscriptionFound = subscriptionRepository.findById(subscriptionId).orElseThrow(()-> new NoSuchElementException("There is no subscription with id " + subscriptionId));
        return SubscriptionDTO.builder()
                .endDate(subscriptionFound.getEndDate())
                .startDate(subscriptionFound.getStartDate())
                .price(subscriptionFound.getPrice())
                .status(subscriptionFound.getStatus()).build();
    }

    public List<SubscriptionDTO> getAllSubscription() {
        return subscriptionMapper.toDTOList(subscriptionRepository.findAll());
    }

    public List<SubscriptionDTO> getAllActiveSubscriptions() {
        return subscriptionMapper.toDTOList(subscriptionRepository.findByStatus(true));
    }

    public List<SubscriptionDTO> getAlleSubscriptionsByType(SubscriptionType type) {
       return subscriptionMapper.toDTOList(subscriptionRepository.findBySubscriptionType(type));
    }

    public SubscriptionDTO adminUpdateSubscriptionEndDate(Long subscriptionId, LocalDate date) throws NoSuchElementException{
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(()-> new NoSuchElementException("There is no subscription with id " + subscriptionId));
        subscription.setEndDate(date);
        subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(subscription);
    }
}
