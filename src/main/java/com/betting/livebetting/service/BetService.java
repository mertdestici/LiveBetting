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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@RequiredArgsConstructor
@Transactional
@Service
public class BetService {

    private final MatchEventRepository matchEventRepository;
    private final BetRepository betRepository;
    private final UserRepository userRepository;

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final static int MAX_MULTIPLIER = 500;
    private final static double MAX_TOTAL_INVESTMENT = 10000.0;

    public BetResponseDto placeBet(BetRequestDto request, String username) throws Exception {
        Callable<BetResponseDto> task = () -> processBet(request, username);
        Future<BetResponseDto> future = executor.submit(task);

        try {
            return future.get(2, TimeUnit.SECONDS); // timeout
        } catch (TimeoutException e) {
            throw new RuntimeException("Bet processing timed out");
        }
    }

    protected BetResponseDto processBet(BetRequestDto request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MatchEvent event = matchEventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        double currentOdds = getOddsByType(event, request.getBetType());

        long totalMultiplier = betRepository.sumMultiplierByEventId(request.getEventId());
        if ((totalMultiplier + request.getMultiplier()) > MAX_MULTIPLIER) {
            throw new RuntimeException("Too many bets on this match");
        }

        double totalInvestment = request.getStakeAmount() * request.getMultiplier();
        if (totalInvestment > MAX_TOTAL_INVESTMENT) {
            throw new RuntimeException("Total investment exceeds allowed limit");
        }

        Bet bet = Bet.builder()
                .eventId(request.getEventId())
                .betType(request.getBetType())
                .multiplier(request.getMultiplier())
                .stakeAmount(request.getStakeAmount())
                .lockedOdd(currentOdds)
                .betTime(LocalDateTime.now())
                .user(user)
                .build();

        Bet saved = betRepository.save(bet);

        return BetResponseDto.builder()
                .betId(saved.getId())
                .eventId(saved.getEventId())
                .betType(saved.getBetType())
                .lockedOdd(saved.getLockedOdd())
                .totalInvestment(totalInvestment)
                .build();
    }

    private double getOddsByType(MatchEvent event, BetType type) {
        return switch (type) {
            case HOME_WIN -> event.getHomeWinOdds();
            case DRAW -> event.getDrawOdds();
            case AWAY_WIN -> event.getAwayWinOdds();
        };
    }
}