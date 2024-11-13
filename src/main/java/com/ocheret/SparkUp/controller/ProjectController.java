package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.service.LinkVerificationService;
import com.ocheret.SparkUp.service.ProjectService;
import com.ocheret.SparkUp.exception.TaskStateException;
import com.ocheret.SparkUp.repository.UserRepository;
import com.ocheret.SparkUp.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final LinkVerificationService linkVerificationService;
    private final UserRepository userRepository;
    
    @Autowired
    private ProjectService projectService;

    public ProjectController(LinkVerificationService linkVerificationService, UserRepository userRepository) {
        this.linkVerificationService = linkVerificationService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project, Authentication authentication) {
        if (project.getLinkToProject() != null && !linkVerificationService.isLinkActive(project.getLinkToProject())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "The provided project link is not active or reachable.");
            return ResponseEntity.badRequest().body(response);
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        project.setOwner(currentUser);
        Project savedProject = projectService.createProject(project, currentUser);
        return ResponseEntity.ok(savedProject);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Project> updateProjectFields(
            @PathVariable Long projectId,
            @RequestBody Map<String, Object> updates) {
        Project updatedProject = projectService.updateProjectFields(projectId, updates);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        Project project = projectService.getProjectById(projectId);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{projectId}/tasks/{taskId}/approve")
    public ResponseEntity<?> approveTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new TaskStateException("User not found"));
                
            projectService.approveTask(projectId, taskId, currentUser);
            return ResponseEntity.ok(projectService.getProjectById(projectId));
        } catch (TaskStateException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{projectId}/tasks/{taskId}/deny")
    public ResponseEntity<Project> denyTask(
            @PathVariable Long projectId, 
            @PathVariable Long taskId,
            @RequestBody Map<String, String> request) {
        String denialComment = request.get("denialComment");
        if (denialComment == null) {
            return ResponseEntity.badRequest().build();
        }
        projectService.denyTask(projectId, taskId, denialComment);
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }
}
