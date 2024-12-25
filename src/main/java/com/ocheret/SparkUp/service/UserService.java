package com.ocheret.SparkUp.service;

import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.repository.UserRepository;
import com.ocheret.SparkUp.entity.WorkExperience;

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

    public User addWorkExperience(String username, WorkExperience workExperience) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.getWorkExperience().add(workExperience);
        return userRepository.save(user);
    }

    public User updateWorkExperience(String username, Long experienceId, WorkExperience updatedExperience) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.getWorkExperience().stream()
            .filter(exp -> exp.getId().equals(experienceId))
            .findFirst()
            .ifPresent(existingExperience -> {
                existingExperience.setJobTitle(updatedExperience.getJobTitle());
                existingExperience.setCompany(updatedExperience.getCompany());
                existingExperience.setDateEmploymentStart(updatedExperience.getDateEmploymentStart());
                existingExperience.setDateEmploymentFinish(updatedExperience.getDateEmploymentFinish());
                existingExperience.setPresentEmployment(updatedExperience.isPresentEmployment());
                existingExperience.setJobSummary(updatedExperience.getJobSummary());
                existingExperience.setKeyAchievements(updatedExperience.getKeyAchievements());
                existingExperience.setRecommendations(updatedExperience.getRecommendations());
            });

        return userRepository.save(user);
    }

    public User toggleWorkExperienceVisibility(String username, Long experienceId, boolean visible) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        WorkExperience experience = user.getWorkExperience().stream()
            .filter(exp -> exp.getId().equals(experienceId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Work experience not found"));

       // experience.setVisible(visible);
        return userRepository.save(user);
    }
} 