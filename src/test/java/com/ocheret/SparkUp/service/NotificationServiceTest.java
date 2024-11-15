package com.ocheret.SparkUp.service;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.NotificationRepository;
import com.ocheret.SparkUp.entity.Notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;





@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;
    
    @InjectMocks
    private NotificationService notificationService;
    
    @Test
    void createTaskCompletionNotification_Success() {
        // Test setup
        Project project = new Project();
        Task task = new Task();
        User owner = new User();
        
        // Test execution
        notificationService.createTaskCompletionNotification(task, project, owner);
        
        // Verification
        verify(notificationRepository).save(any(Notification.class));
    }
} 