package com.team1.dealerApp.video.tvshow;

import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TvShowServiceTest {

	private final TvShowMapper tvShowMapper = new TvShowMapper();

	@Mock
	private TvShowRepository tvShowRepository;

	@Mock
	private TvShowMapper tvShowMapperInj;

	@Mock
	private TvShowUpdater<Object> tvShowUpdater;

	@InjectMocks
	private TvShowService tvShowService;

	private TvShow purchasableShow;
	private TvShow rentableShow;
	private TvShowDTO purchasableShowDTO;
	private TvShowDTO rentableShowDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		List <String> cast1= Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
		List <String> cast2= Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");

		purchasableShow = new TvShow("Breaking bad", Genre.DRAMA, cast1, "Vince Gilligan", Year.of(2006), 50.0, 15.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.PURCHASABLE, 6, 52);
		rentableShow = new TvShow("The boys", Genre.ACTION, cast2, "Erik Kripke", Year.of(2019), 60.0, 20.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 5, 40);

		purchasableShowDTO = tvShowMapper.toTvShowDTO(purchasableShow);
		rentableShowDTO = tvShowMapper.toTvShowDTO(rentableShow);
	}

	@Test
	void testAddShow() throws BadRequestException {
		when(tvShowRepository.existsByTitleAndDirector(purchasableShow.getTitle(), purchasableShow.getDirector())).thenReturn(false);
		TvShowDTO result = tvShowService.addTvShow(purchasableShowDTO);

		assertEquals(purchasableShowDTO, result);
	}

	@Test
	void testAddShow_WhenShowAlreadyExists() {
		when(tvShowRepository.existsByTitleAndDirector(purchasableShowDTO.getTitle(), purchasableShowDTO.getDirector())).thenReturn(true);
		assertThrows(BadRequestException.class, () -> tvShowService.addTvShow(purchasableShowDTO));
	}
}