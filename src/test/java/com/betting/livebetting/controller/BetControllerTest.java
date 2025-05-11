package com.betting.livebetting.controller;

import com.betting.livebetting.config.SecurityConfig;
import com.betting.livebetting.dto.BetRequestDto;
import com.betting.livebetting.dto.BetResponseDto;
import com.betting.livebetting.model.BetType;
import com.betting.livebetting.repository.BetRepository;
import com.betting.livebetting.repository.MatchEventRepository;
import com.betting.livebetting.repository.UserRepository;
import com.betting.livebetting.security.AuthFilter;
import com.betting.livebetting.security.CustomUserDetailsService;
import com.betting.livebetting.service.BetService;
import com.betting.livebetting.service.BulletinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BetController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class BetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthFilter authFilter;
    @MockitoBean
    private BetService betService;
    @MockitoBean
    private BetRepository betRepository;
    @MockitoBean
    private MatchEventRepository matchEventRepository;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "user1", roles = "CUSTOMER")
    void shouldPlaceBetSuccessfully() throws Exception {
        // Arrange
        BetRequestDto requestDto = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .multiplier(2)
                .stakeAmount(50.0)
                .build();

        BetResponseDto responseDto = BetResponseDto.builder()
                .betId(100L)
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .lockedOdd(1.85)
                .totalInvestment(100.0)
                .build();

        when(betService.placeBet(any(BetRequestDto.class), eq("user1")))
                .thenReturn(responseDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // Act & Assert
        mockMvc.perform(post("/api/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betId").value(100));
    }

    @Test
    void shouldReturnUnauthorizedIfUnauthorized() throws Exception {
        BetRequestDto requestDto = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .multiplier(2)
                .stakeAmount(50.0)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user1", roles = "CUSTOMER")
    void shouldReturnConflictIfBetFails() throws Exception {
        BetRequestDto requestDto = BetRequestDto.builder()
                .eventId(1L)
                .betType(BetType.HOME_WIN)
                .multiplier(2)
                .stakeAmount(50.0)
                .build();

        when(betService.placeBet(any(BetRequestDto.class), eq("user1")))
                .thenThrow(new RuntimeException("Odds have changed. Please confirm."));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
}
