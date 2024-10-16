package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project, Authentication authentication) {
        User owner = (User) authentication.getPrincipal();  // Get the logged-in user
        project.setOwner(owner);
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project, Authentication authentication) {
        Project existingProject = projectService.getProjectById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Ensure the logged-in user is the owner
        if (!existingProject.getOwner().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).build();  // Forbidden
        }

        project.setOwner(existingProject.getOwner());
        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, Authentication authentication) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Ensure the logged-in user is the owner
        if (!project.getOwner().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).build();  // Forbidden
        }

        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
