package com.betting.livebetting.controller;
import com.betting.livebetting.dto.BetRequestDto;
import com.betting.livebetting.dto.BetResponseDto;
import com.betting.livebetting.service.BetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    @PostMapping
    public ResponseEntity<?> placeBet(@RequestBody BetRequestDto request, Authentication authentication) {
        try {
            BetResponseDto response = betService.placeBet(request, customerId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }
}
