package com.team1.dealerApp.video.tvshow;

import com.team1.dealerApp.utils.Pager;
import com.team1.dealerApp.video.AgeRating;
import com.team1.dealerApp.video.Genre;
import com.team1.dealerApp.video.VideoStatus;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TvShowServiceTest {

    private final TvShowMapper tvShowMapper = new TvShowMapper();


    @InjectMocks
    private TvShowService tvShowService;

    @Mock
    private TvShowRepository tvShowRepository;

    @Mock
    private TvShowMapper tvShowMapperInj;

    @Mock
    private TvShowUpdater<Object> tvShowUpdater;

    @Mock
    private Pager pager;


    private TvShowDTO purchasableTvShowDTO;
    private TvShow purchasableTvShow;
    private TvShow rentableTvShow;
    private CreateShowDTO purchasableCreateShowDTO;
    private Page<TvShow> pageTvShow;


    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        List<String> cast1 = Arrays.asList("Bryan Cranston", "Aaron Paul", "Giancarlo Esposito");
        List<String> cast2 = Arrays.asList("Antony Starr", "Karl Urban", "Jack Quaid");

        purchasableTvShow = new TvShow("Breaking bad", Genre.DRAMA, cast1, "Vince Gilligan", Year.of(2006), 50.0, 15.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.PURCHASABLE, 0, 0.0, AgeRating.NC17, 6, 52);
        rentableTvShow = new TvShow("The boys", Genre.ACTION, cast2, "Erik Kripke", Year.of(2019), 60.0, 20.0, "Patriota impazzisce", 4.6f, VideoStatus.RENTABLE, 0, 0.0, AgeRating.R, 5, 40);

        purchasableTvShowDTO = tvShowMapper.toTvShowDTO(purchasableTvShow);

        purchasableCreateShowDTO = new CreateShowDTO("Breaking bad", Genre.DRAMA, cast1, "Vince Gilligan", Year.of(2006), 50.0, 15.0, "Un prof si ammala e inizia a fare la droga", 5.0f, VideoStatus.PURCHASABLE, 0, 0.0, AgeRating.NC17, 6, 52);

        pageTvShow = new PageImpl<>(Collections.singletonList(purchasableTvShow));
    }


    @Test
    public void testAddTvShow() throws BadRequestException {

        when(tvShowRepository.existsByTitleAndDirector(purchasableTvShow.getTitle(), purchasableTvShow.getDirector())).thenReturn(false);
        when(tvShowRepository.save(any(TvShow.class))).thenReturn(purchasableTvShow);
        when(tvShowMapperInj.toTvShow(any(CreateShowDTO.class))).thenReturn(purchasableTvShow);
        when(tvShowMapperInj.toTvShowDTO(any(TvShow.class))).thenReturn(purchasableTvShowDTO);

        TvShowDTO result = tvShowService.addTvShow(purchasableCreateShowDTO);


        assertEquals(purchasableTvShowDTO, result);
    }

    @Test
    public void testAddTvShow_whenTvShowAlreadyExists() {
        when(tvShowRepository.existsByTitleAndDirector(purchasableTvShowDTO.getTitle(), purchasableTvShowDTO.getDirector())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> tvShowService.addTvShow(purchasableCreateShowDTO));
    }


    @Test
    public void testDeleteShowById() {
        tvShowService.deleteShowById(1L);

        verify(tvShowRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testGetAllShows() throws BadRequestException {

        int page = 0;
        int size = 10;

        when(tvShowRepository.findAll()).thenReturn(Arrays.asList(purchasableTvShow, rentableTvShow));
        when(pager.createPageable(anyInt(), anyInt())).thenReturn(PageRequest.of(page, size));
        when(tvShowRepository.findAll(pager.createPageable(page, size))).thenReturn(pageTvShow);

        Page<TvShowDTO> moviePage = tvShowService.getAllShows(page, size);
        List<TvShowDTO> result = moviePage.getContent();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllShows_NoShowsFound() {

        int page = 0;
        int size = 10;

        when(tvShowRepository.findAll(PageRequest.of(page, size))).thenReturn(Page.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> tvShowService.getAllShows(page, size));

        assertEquals("There are no shows!", exception.getMessage());
    }


    @Test
    public void testGetShowDTOById() {

        when(tvShowRepository.findById(anyLong())).thenReturn(Optional.of(purchasableTvShow));

        tvShowService.getShowDTOById(1L);

        verify(tvShowRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetShowDTOById_NotFound() {

        when(tvShowRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> tvShowService.getShowDTOById(1L));

        assertEquals("No show with id: 1", exception.getMessage());
    }


    @Test
    public void testUpdateShow() {
        purchasableTvShowDTO.setTitle("Breaking bad");

        when(tvShowRepository.existsById(1L)).thenReturn(true);
        when(tvShowMapperInj.toTvShow(any(CreateShowDTO.class))).thenReturn(purchasableTvShow);
        when(tvShowRepository.save(any(TvShow.class))).thenReturn(purchasableTvShow);
        when(tvShowMapperInj.toTvShowDTO(purchasableTvShow)).thenReturn(purchasableTvShowDTO);

        TvShowDTO result = tvShowService.updateShow(purchasableCreateShowDTO, 1L);

        assertEquals("Breaking bad", result.getTitle());
    }

    @Test
    public void testUpdateShow_NotFound() {
        when(tvShowRepository.existsById(1L)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> tvShowService.updateShow(purchasableCreateShowDTO, 1L));

        assertEquals("There is no show with id: 1", exception.getMessage());
    }


    @Test
    public void testUpdateShowField() throws Exception {

        when(tvShowRepository.findById(1L)).thenReturn(Optional.of(purchasableTvShow));

        TvShow updatedTvShow = new TvShow();
        when(tvShowUpdater.updateShowField(any(TvShow.class), anyString(), any())).thenReturn(updatedTvShow);
        when(tvShowMapperInj.toTvShowDTO(updatedTvShow)).thenReturn(new TvShowDTO());

        tvShowService.updateShowField(1L, "New Title", "title");

        verify(tvShowRepository, times(1)).save(updatedTvShow);
    }

    @Test
    public void testUpdateShowField_NoMovieFound() {
        when(tvShowRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> tvShowService.updateShowField(1L, "New Title", "title"));

        assertEquals("No show with id: 1", exception.getMessage());
    }

}
