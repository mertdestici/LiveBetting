package com.betting.livebetting.service;

import com.betting.livebetting.dto.MatchEventRequestDto;
import com.betting.livebetting.dto.MatchEventResponseDto;
import com.betting.livebetting.model.MatchEvent;
import com.betting.livebetting.repository.MatchEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BulletinServiceTest {

    @Mock
    private MatchEventRepository matchEventRepository;
    @InjectMocks
    private BulletinService bulletinService;

    @Test
    void shouldCreateMatchEventSuccessfully() {
        // Arrange
        MatchEventRequestDto request = MatchEventRequestDto.builder()
                .leagueName("Premier League")
                .homeTeam("Arsenal")
                .awayTeam("Chelsea")
                .homeWinOdds(1.75)
                .drawOdds(3.10)
                .awayWinOdds(2.25)
                .startTime(LocalDateTime.now())
                .build();

        MatchEvent saved = MatchEvent.builder()
                .id(1L)
                .leagueName(request.getLeagueName())
                .homeTeam(request.getHomeTeam())
                .awayTeam(request.getAwayTeam())
                .homeWinOdds(request.getHomeWinOdds())
                .drawOdds(request.getDrawOdds())
                .awayWinOdds(request.getAwayWinOdds())
                .startTime(request.getStartTime())
                .build();

        when(matchEventRepository.save(any())).thenReturn(saved);

        // Act
        MatchEventResponseDto response = bulletinService.createMatchEvent(request);

        // Assert
        assertNotNull(response);
        assertEquals("Arsenal", response.getHomeTeam());
        assertEquals("Chelsea", response.getAwayTeam());
        verify(matchEventRepository).save(any());
    }

    @Test
    void shouldReturnAllMatchEvents() {
        // Arrange
        MatchEvent match1 = MatchEvent.builder().id(1L).homeTeam("A").build();
        MatchEvent match2 = MatchEvent.builder().id(2L).homeTeam("B").build();

        when(matchEventRepository.findAll()).thenReturn(List.of(match1, match2));

        // Act
        List<MatchEventResponseDto> result = bulletinService.getAllMatchEvents();

        // Assert
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getHomeTeam());
        assertEquals("B", result.get(1).getHomeTeam());
        verify(matchEventRepository).findAll();
    }
}
