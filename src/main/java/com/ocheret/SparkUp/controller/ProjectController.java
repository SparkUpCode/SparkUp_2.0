package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project savedProject = projectService.createProject(project);
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

    @GetMapping("/creator/{username}")
    public ResponseEntity<List<Project>> getProjectsByCreator(@PathVariable String username) {
        List<Project> projects = projectRepository.findByCreatorUsername(username);
        return ResponseEntity.ok(projects);
    }
}
