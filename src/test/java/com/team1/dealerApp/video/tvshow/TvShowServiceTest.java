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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
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

	List <String> cast1= Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
	List <String> cast2= Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		purchasableShow = new TvShow("Breaking bad", Genre.DRAMA, cast1, "Vince Gilligan", Year.of(2006), 50.0, 15.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.PURCHASABLE, 6, 52);
		rentableShow = new TvShow("The boys", Genre.ACTION, cast2, "Erik Kripke", Year.of(2019), 60.0, 20.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 5, 40);

		purchasableShowDTO = tvShowMapper.toTvShowDTO(purchasableShow);
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

	@Test
	void testGetAllShows () throws BadRequestException {
		when(tvShowRepository.findAll()).thenReturn(Arrays.asList(purchasableShow, rentableShow));

		List<TvShowDTO> found = tvShowService.getAllShows();

		assertEquals(2, found.size());
	}

	@Test
	void testGetAllShows_NoShowFound (){
		when(tvShowRepository.findAll()).thenReturn(List.of());

		assertThrows(BadRequestException.class, () -> tvShowService.getAllShows());
	}

	@Test
	void testGetShowById () throws BadRequestException {

		when(tvShowRepository.findById(anyLong())).thenReturn(Optional.of(purchasableShow));
		when(tvShowMapperInj.toTvShowDTO(purchasableShow)).thenReturn(purchasableShowDTO);

		TvShowDTO found = tvShowService.getShowById(1L);

		assertEquals(purchasableShowDTO, found);
	}

	@Test
	void testGetShowById_NoShowFound () {
		when(tvShowRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(BadRequestException.class, () -> tvShowService.getShowById(1L));
	}

	@Test
	void testUpdateShow () throws NoSuchElementException{
		purchasableShowDTO.setPlot("Bella serie");

		when(tvShowRepository.existsById(anyLong())).thenReturn(true);
		when(tvShowMapperInj.toTvShow(purchasableShowDTO)).thenReturn(purchasableShow);
		when(tvShowMapperInj.toTvShowDTO(purchasableShow)).thenReturn(purchasableShowDTO);

		TvShowDTO updated = tvShowService.updateShow(purchasableShowDTO, 1L);

		assertEquals("Bella serie", updated.getPlot());
	}

	@Test
	void testUpdateShow_ShowNotFound () {
		when(tvShowRepository.existsById(anyLong())).thenReturn(false);

		assertThrows(NoSuchElementException.class, () -> tvShowService.updateShow(purchasableShowDTO, 1L));
	}

	@Test
	void testUpdateShowField () throws NoSuchFieldException, IllegalAccessException, NoSuchElementException {
		when(tvShowRepository.findById(anyLong())).thenReturn(Optional.of(rentableShow));

		TvShow updatedShow = new TvShow("The boys", Genre.DRAMA, cast2, "Erik Kripke", Year.of(2019), 60.0, 20.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 5, 40);

		when(tvShowUpdater.updateShowField(rentableShow, "genre", Genre.DRAMA)).thenReturn(updatedShow);
		when(tvShowMapperInj.toTvShowDTO(updatedShow)).thenReturn(tvShowMapper.toTvShowDTO(updatedShow));

		TvShowDTO edited = tvShowService.updateShowField(1L, Genre.DRAMA, "genre" );

		assertEquals(Genre.DRAMA, edited.getGenre());
	}

	@Test
	void testUpdateShowField_ShowNotFound (){
		when(tvShowRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(NoSuchElementException.class, () -> tvShowService.updateShowField(1L, Genre.DRAMA, "genre" ));
	}

}