package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.service.ProjectService;
import com.ocheret.SparkUp.exception.UnauthorizedAccessException;
import com.ocheret.SparkUp.entity.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(e.getMessage());
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project savedProject = projectService.createProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Project> updateProjectFields(
            @PathVariable Long projectId,
            @RequestBody Map<String, Object> updates) {
        try {
            System.out.println("Received updates: " + updates);
            Project updatedProject = projectService.updateProjectFields(projectId, updates);
            return ResponseEntity.ok(updatedProject);
        } catch (UnauthorizedAccessException e) {
            throw e; // Will be handled by the exception handler
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
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

    @GetMapping("/creator/{username}")
    public ResponseEntity<List<Project>> getProjectsByCreator(@PathVariable String username) {
        List<Project> projects = projectRepository.findByCreatorUsername(username);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{projectId}/tasks/{taskId}/assign")
    public ResponseEntity<Task> assignTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        try {
            Task updatedTask = projectService.assignTask(projectId, taskId);
            return ResponseEntity.ok(updatedTask);
        } catch (UnauthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
    }
}
