package com.team1.dealerApp.subscription;


import com.team1.dealerApp.utils.Pager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final Pager pager;

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

    public Page<SubscriptionDTO> getAllSubscription(int pageNumber, int size) {
        return subscriptionRepository
                .findAll(pager.createPageable(pageNumber, size))
                .map(subscriptionMapper::toDTO);
    }

    public Page <SubscriptionDTO> getAllActiveSubscriptions( int pageNumber, int size) {
        return subscriptionRepository
                .findByStatusOrderByStatusAsc(true, pager.createPageable(pageNumber, size))
                .map(subscriptionMapper::toDTO);
    }

    public Page<SubscriptionDTO> getAlleSubscriptionsByType(SubscriptionType type, int pageNumber, int size) {
       return subscriptionRepository
               .findBySubscriptionType(type, pager.createPageable(pageNumber, size))
               .map(subscriptionMapper::toDTO);
    }

    public SubscriptionDTO adminUpdateSubscriptionEndDate(Long subscriptionId, LocalDate date) throws NoSuchElementException{
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(()-> new NoSuchElementException("There is no subscription with id " + subscriptionId));
        subscription.setEndDate(date);
        subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(subscription);
    }

    public SubscriptionDTO addSubscription(Subscription subscription){
        subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(subscription);
    }

    public SubscriptionDTO updateSubscriptionStatus(Long orderId, boolean status) {
        Subscription subscription = subscriptionRepository.findById(orderId).orElseThrow(()-> new NoSuchElementException("There is no order with id " + orderId));
        subscription.setStatus(status);
        return subscriptionMapper.toDTO(subscription);
    }
}
