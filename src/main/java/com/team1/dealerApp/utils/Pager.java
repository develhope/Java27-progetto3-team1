package com.team1.dealerApp.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class Pager {

	public Pageable createPageable( int pageNumber, int size){
		return PageRequest.of(pageNumber, size);
	}
}
