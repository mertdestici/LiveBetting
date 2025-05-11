package com.betting.livebetting.service;

import com.betting.livebetting.model.User;
import com.betting.livebetting.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        String username = "newuser";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Act
        String result = userService.register(username, rawPassword);

        // Assert
        assertEquals("User registered successfully.", result);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals("CUSTOMER", savedUser.getRole());
    }

    @Test
    void shouldThrowExceptionIfUsernameExists() {
        // Arrange
        String username = "existing";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.register(username, "pass"));
        assertEquals("Username already exists.", ex.getMessage());

        verify(userRepository, never()).save(any());
    }
}
