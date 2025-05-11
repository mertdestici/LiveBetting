package com.betting.livebetting.controller;

import com.betting.livebetting.dto.BetRequestDto;
import com.betting.livebetting.dto.BetResponseDto;
import com.betting.livebetting.service.BetService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    @PostMapping
    public ResponseEntity<?> placeBet(@RequestBody BetRequestDto request, Authentication authentication) throws Exception {
        String customerName = authentication.getName();
        BetResponseDto response = betService.placeBet(request, customerName);
        return ResponseEntity.ok(response);
    }
}
