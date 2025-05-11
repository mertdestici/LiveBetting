package com.betting.livebetting.util;

import com.betting.livebetting.model.MatchEvent;
import com.betting.livebetting.repository.MatchEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class OddsUpdateSchedulerTest {

    @Mock
    private MatchEventRepository matchEventRepository;
    @InjectMocks
    private OddsUpdateScheduler scheduler;

    @Test
    void shouldUpdateOddsForAllEvents() {
        // Arrange
        MatchEvent event1 = MatchEvent.builder()
                .id(1L)
                .homeWinOdds(1.5)
                .drawOdds(3.0)
                .awayWinOdds(2.8)
                .build();

        MatchEvent event2 = MatchEvent.builder()
                .id(2L)
                .homeWinOdds(2.0)
                .drawOdds(2.9)
                .awayWinOdds(3.1)
                .build();

        when(matchEventRepository.findAll()).thenReturn(List.of(event1, event2));

        // Act
        scheduler.updateOdds();

        // Assert
        verify(matchEventRepository).findAll();
        verify(matchEventRepository).saveAll(any());

        assertNotEquals(1.5, event1.getHomeWinOdds());
        assertNotEquals(2.0, event2.getHomeWinOdds());
    }
}
