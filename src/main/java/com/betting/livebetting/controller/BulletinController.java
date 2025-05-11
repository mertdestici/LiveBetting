package com.betting.livebetting.controller;

import com.betting.livebetting.dto.MatchEventRequestDto;
import com.betting.livebetting.dto.MatchEventResponseDto;
import com.betting.livebetting.service.BulletinService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bulletin")
public class BulletinController {

    private final BulletinService bulletinService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MatchEventResponseDto> createMatch(@RequestBody MatchEventRequestDto request) {
        MatchEventResponseDto response = bulletinService.createMatchEvent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/getAllMatches")
    public ResponseEntity<List<MatchEventResponseDto>> getAllMatches() {
        return ResponseEntity.ok(bulletinService.getAllMatchEvents());
    }
}