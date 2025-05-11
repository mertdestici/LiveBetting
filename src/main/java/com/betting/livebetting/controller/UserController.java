package com.betting.livebetting.controller;

import com.betting.livebetting.dto.UserRequestDto;
import com.betting.livebetting.repository.UserRepository;
import com.betting.livebetting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto request) {
        String result = userService.register(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(result);
    }
}
