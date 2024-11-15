package com.ocheret.SparkUp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.entity.Task;



import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskController taskController;

    @Test
    void addTask_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setTasks(new ArrayList<>());

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        ResponseEntity<?> response = taskController.addTask(projectId, task);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task savedTask = (Task) response.getBody();
        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals(Task.TaskStatus.ACTIVE, savedTask.getStatus());
        assertFalse(savedTask.isCompleted());
    }
} 