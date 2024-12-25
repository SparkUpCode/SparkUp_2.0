package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.service.UserService;
import com.ocheret.SparkUp.entity.WorkExperience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserWithAssignments(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/me/work-experience")
    public ResponseEntity<?> addWorkExperience(@RequestBody WorkExperience workExperience) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User updatedUser = userService.addWorkExperience(username, workExperience);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/me/work-experience/{experienceId}")
    public ResponseEntity<?> updateWorkExperience(
            @PathVariable Long experienceId,
            @RequestBody WorkExperience workExperience) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User updatedUser = userService.updateWorkExperience(username, experienceId, workExperience);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/me/work-experience/{experienceId}/visibility")
    public ResponseEntity<?> toggleWorkExperienceVisibility(
            @PathVariable Long experienceId,
            @RequestBody Map<String, Boolean> visibility) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User updatedUser = userService.toggleWorkExperienceVisibility(
            username, 
            experienceId, 
            visibility.get("visible")
        );
        return ResponseEntity.ok(updatedUser);
    }
} 