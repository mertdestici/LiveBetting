package com.betting.livebetting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String leagueName;
    private String homeTeam;
    private String awayTeam;
    private Double homeWinOdds;
    private Double drawOdds;
    private Double awayWinOdds;
    private LocalDateTime startTime;
    @Version
    private Integer version;
}