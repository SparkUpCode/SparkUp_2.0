package com.ocheret.SparkUp.service;

import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import java.util.Optional;



@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void getUserById_Success() {
        // Test setup
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        // Test execution
        Optional<User> result = userService.getUserById(userId);
        
        // Verification
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }
} 