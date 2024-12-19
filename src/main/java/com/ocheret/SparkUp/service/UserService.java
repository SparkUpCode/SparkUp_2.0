package com.ocheret.SparkUp.service;

import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public User getUserWithAssignments(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch assigned tasks
        List<Task> assignedTasks = userRepository.findAssignedTasksByUsername(username);
        user.setAssignedTasks(assignedTasks);

        return user;
    }
} 