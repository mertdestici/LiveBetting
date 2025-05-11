package com.betting.livebetting.controller;

import com.betting.livebetting.config.SecurityConfig;
import com.betting.livebetting.dto.MatchEventRequestDto;
import com.betting.livebetting.dto.MatchEventResponseDto;
import com.betting.livebetting.repository.MatchEventRepository;
import com.betting.livebetting.repository.UserRepository;
import com.betting.livebetting.security.AuthFilter;
import com.betting.livebetting.security.CustomUserDetailsService;
import com.betting.livebetting.service.BulletinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BulletinController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class BulletinControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthFilter authFilter;
    @MockitoBean
    private BulletinService bulletinService;
    @MockitoBean
    private MatchEventRepository matchEventRepository;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN") // ✅ Authenticated admin
    void shouldCreateMatchEventWhenAuthorized() throws Exception {
        MatchEventResponseDto mockResponse = new MatchEventResponseDto();
        mockResponse.setId(1L);
        mockResponse.setLeagueName("Premier League");
        mockResponse.setHomeTeam("Arsenal");
        mockResponse.setAwayTeam("Chelsea");
        mockResponse.setHomeWinOdds(1.75);
        mockResponse.setDrawOdds(3.00);
        mockResponse.setAwayWinOdds(2.10);
        mockResponse.setStartTime(LocalDateTime.now());

        Mockito.when(bulletinService.createMatchEvent(any())).thenReturn(mockResponse);

        String requestBody = objectMapper.writeValueAsString(mockResponse);

        mockMvc.perform(post("/api/bulletin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeTeam").value("Arsenal"));
    }

    @Test
    void shouldReturnUnauthorizedIfNotAuthenticatedForPost() throws Exception {
        MatchEventRequestDto matchEventRequestDto = MatchEventRequestDto.builder()
                .leagueName("Premier League")
                .homeTeam("Arsenal")
                .awayTeam("Chelsea")
                .homeWinOdds(1.75)
                .drawOdds(3.00)
                .awayWinOdds(2.10)
                .startTime(LocalDateTime.now())
                .build();

        String requestBody = objectMapper.writeValueAsString(matchEventRequestDto);

        mockMvc.perform(post("/api/bulletin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnAllMatchEvents() throws Exception {
        MatchEventResponseDto match1 = new MatchEventResponseDto();
        match1.setId(1L);
        match1.setHomeTeam("Arsenal");

        MatchEventResponseDto match2 = new MatchEventResponseDto();
        match2.setId(2L);
        match2.setHomeTeam("Liverpool");

        Mockito.when(bulletinService.getAllMatchEvents()).thenReturn(List.of(match1, match2));

        mockMvc.perform(get("/api/bulletin/getAllMatches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
