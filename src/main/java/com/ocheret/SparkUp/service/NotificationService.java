package com.ocheret.SparkUp.service;

import com.ocheret.SparkUp.entity.Notification;
import com.ocheret.SparkUp.repository.NotificationRepository;
import com.ocheret.SparkUp.repository.UserRepository;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;

    public void createTaskCompletionNotification(Task task, Project project, User projectOwner) {
        Notification notification = new Notification();
        notification.setUser(projectOwner);
        notification.setProject(project);
        notification.setTask(task);
        notification.setMessage("Task '" + task.getTitle() + "' has been completed with comment: " + task.getCompletionComment());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return userRepository.findById(userId)
            .map(user -> notificationRepository.findByUserOrderByCreatedAtDesc(user))
            .orElse(Collections.emptyList());
    }
} 