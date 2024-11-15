package com.ocheret.SparkUp.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET_KEY);
    }

    @Test
    void generateAndValidateToken_Success() {
        // Arrange
        String username = "testUser";

        // Act
        String token = jwtUtil.generateToken(username);
        
        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token, username));
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void validateToken_InvalidUsername() {
        // Arrange
        String username = "testUser";
        String wrongUsername = "wrongUser";
        String token = jwtUtil.generateToken(username);

        // Act & Assert
        assertFalse(jwtUtil.validateToken(token, wrongUsername));
    }

    @Test
    void extractUsername_Success() {
        // Arrange
        String username = "testUser";
        
        // Act
        String token = jwtUtil.generateToken(username);
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(username, extractedUsername);
    }
} 