package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.service.LinkVerificationService;
import com.ocheret.SparkUp.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final LinkVerificationService linkVerificationService;

    @Autowired
    private ProjectService projectService;
    public ProjectController(LinkVerificationService linkVerificationService) {
        this.linkVerificationService = linkVerificationService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        if (project.getLinkToProject() != null && !linkVerificationService.isLinkActive(project.getLinkToProject())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "The provided project link is not active or reachable.");
            return ResponseEntity.badRequest().body(response); // Return a 400 response with error message
        }
        else {
            Project savedProject = projectService.createProject(project);
            return ResponseEntity.ok(savedProject);
        }
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
}
