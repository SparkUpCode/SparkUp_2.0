package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.UserRepository;
import com.ocheret.SparkUp.security.JwtUtil;
import com.ocheret.SparkUp.dto.AuthRequest;
import com.ocheret.SparkUp.dto.AuthResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.Mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = authController.registerUser(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        // Arrange
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("password");

        when(jwtUtil.generateToken("testUser")).thenReturn("testToken");

        // Act
        ResponseEntity<?> response = authController.loginUser(authRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("testToken", authResponse.getToken());
    }
} 