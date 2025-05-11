package com.betting.livebetting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventResponseDto {
    private Long id;
    private String leagueName;
    private String homeTeam;
    private String awayTeam;
    private Double homeWinOdds;
    private Double drawOdds;
    private Double awayWinOdds;
    private LocalDateTime startTime;
}
