package com.betting.livebetting.service;

import com.betting.livebetting.dto.MatchEventRequestDto;
import com.betting.livebetting.dto.MatchEventResponseDto;
import com.betting.livebetting.model.MatchEvent;
import com.betting.livebetting.repository.MatchEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BulletinService {

    private final MatchEventRepository matchEventRepository;

    public MatchEventResponseDto createMatchEvent(MatchEventRequestDto request) {
        MatchEvent match = MatchEvent.builder()
                .leagueName(request.getLeagueName())
                .homeTeam(request.getHomeTeam())
                .awayTeam(request.getAwayTeam())
                .homeWinOdds(request.getHomeWinOdds())
                .drawOdds(request.getDrawOdds())
                .awayWinOdds(request.getAwayWinOdds())
                .startTime(request.getStartTime())
                .build();

        MatchEvent saved = matchEventRepository.save(match);

        return mapToDto(saved);
    }

    public List<MatchEventResponseDto> getAllMatchEvents() {
        return matchEventRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private MatchEventResponseDto mapToDto(MatchEvent match) {
        return MatchEventResponseDto.builder()
                .id(match.getId())
                .leagueName(match.getLeagueName())
                .homeTeam(match.getHomeTeam())
                .awayTeam(match.getAwayTeam())
                .homeWinOdds(match.getHomeWinOdds())
                .drawOdds(match.getDrawOdds())
                .awayWinOdds(match.getAwayWinOdds())
                .startTime(match.getStartTime())
                .build();
    }
}

