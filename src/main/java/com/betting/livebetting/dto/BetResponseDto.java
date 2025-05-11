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
public class BetResponseDto {
    private Long betId;
    private Long eventId;
    private BetType betType;
    private Double lockedOdd;
    private Double totalInvestment;
}
