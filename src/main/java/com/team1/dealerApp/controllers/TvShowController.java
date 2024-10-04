package com.team1.dealerApp.controllers;

import com.team1.dealerApp.services.TvShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tvshow")
@RequiredArgsConstructor
public class TvShowController {

	private final TvShowService tvShowService;

}
