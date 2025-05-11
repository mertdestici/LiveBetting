package com.betting.livebetting.service;

import com.betting.livebetting.dto.BetRequestDto;
import com.betting.livebetting.dto.BetResponseDto;
import com.betting.livebetting.model.Bet;
import com.betting.livebetting.model.BetType;
import com.betting.livebetting.model.MatchEvent;
import com.betting.livebetting.model.User;
import com.betting.livebetting.repository.BetRepository;
import com.betting.livebetting.repository.MatchEventRepository;
import com.betting.livebetting.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BetServiceTest {

    @Mock
    private MatchEventRepository matchEventRepository;
    @Mock
    private BetRepository betRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BetService betService;

    @Test
    void shouldProcessBetSuccessfully() {
        // Arrange
        User user = new User();
        user.setUsername("user1");

        MatchEvent event = MatchEvent.builder()
                .id(1L)
                .homeWinOdds(1.75)
                .drawOdds(3.2)
                .awayWinOdds(2.5)
                .build();

        BetRequestDto request = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .multiplier(2)
                .stakeAmount(100.0)
                .build();

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(matchEventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(betRepository.sumMultiplierByEventId(1L)).thenReturn(0L);
        when(betRepository.save(any(Bet.class))).thenAnswer(inv -> {
            Bet b = inv.getArgument(0);
            b.setId(10L);
            return b;
        });

        // Act
        BetResponseDto response = betService.processBet(request, "user1");

        // Assert
        assertEquals(10L, response.getBetId());
        assertEquals(1L, response.getEventId());
        assertEquals(200.0, response.getTotalInvestment());
        verify(betRepository).save(any(Bet.class));
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        BetRequestDto request = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .multiplier(1)
                .stakeAmount(100.0)
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                betService.processBet(request, "user1"));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldThrowIfEventNotFound() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(new User()));
        when(matchEventRepository.findById(1L)).thenReturn(Optional.empty());

        BetRequestDto request = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .multiplier(1)
                .stakeAmount(100.0)
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                betService.processBet(request, "user1"));
        assertEquals("Event not found", ex.getMessage());
    }

    @Test
    void shouldThrowIfMultiplierLimitExceeded() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(new User()));
        when(matchEventRepository.findById(1L)).thenReturn(Optional.of(MatchEvent.builder().homeWinOdds(1.3).build()));
        when(betRepository.sumMultiplierByEventId(1L)).thenReturn(499L); // Already near max

        BetRequestDto request = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .multiplier(2)
                .stakeAmount(100.0)
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                betService.processBet(request, "user1"));
        assertEquals("Too many bets on this match", ex.getMessage());
    }

    @Test
    void shouldThrowIfInvestmentLimitExceeded() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(new User()));
        when(matchEventRepository.findById(1L)).thenReturn(Optional.of(
                MatchEvent.builder()
                        .homeWinOdds(1.8)
                        .drawOdds(3.2)
                        .awayWinOdds(2.5)
                        .build()));
        when(betRepository.sumMultiplierByEventId(1L)).thenReturn(0L);

        BetRequestDto request = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.DRAW)
                .multiplier(1)
                .stakeAmount(15000.0) // Too high
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                betService.processBet(request, "user1"));
        assertEquals("Total investment exceeds allowed limit", ex.getMessage());
    }
}
