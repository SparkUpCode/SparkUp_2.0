package com.ocheret.SparkUp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.repository.ProjectRepository;    
import java.util.ArrayList;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping("/{projectId}")
    public ResponseEntity<?> addTask(@PathVariable Long projectId, @RequestBody Task task) {
        return projectRepository.findById(projectId)
            .map(project -> {
                Task newTask = new Task();
                newTask.setTitle(task.getTitle());
                newTask.setDescription(task.getDescription());
                newTask.setStatus(Task.TaskStatus.ACTIVE);
                newTask.setCompleted(false);
                
                project.addTask(newTask);
                Project savedProject = projectRepository.save(project);
                
                Task savedTask = savedProject.getTasks().get(savedProject.getTasks().size() - 1);
                return ResponseEntity.ok(savedTask);
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 