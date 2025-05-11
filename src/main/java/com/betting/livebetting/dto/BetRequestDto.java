package com.betting.livebetting.dto;

import com.betting.livebetting.model.BetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetRequestDto {
    private Long eventId;
    private BetType betType;
    private Integer multiplier;
    private Double stakeAmount;
}
