package com.team1.dealerApp.services;

import com.team1.dealerApp.mappers.TvShowMapper;
import com.team1.dealerApp.repositories.TvShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TvShowService {
	private final TvShowRepository tvShowRepository;
	private final TvShowMapper tvShowMapper;
}
