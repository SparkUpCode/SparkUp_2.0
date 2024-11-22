package com.ocheret.SparkUp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Project project = new Project();
        project.setTitle("Test Project");
        project.setDescription("Test Description");
        project.setPictures(Arrays.asList("http://example.com/image.jpg"));

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.createProject(project, user);

        // Assert
        assertNotNull(result);
        assertEquals("Test Project", result.getTitle());
        assertEquals(user, result.getOwner());
        verify(projectRepository).save(project);
    }

    @Test
    void getProjectById_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setTitle("Test Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        Project result = projectService.getProjectById(projectId);

        // Assert
        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals("Test Project", result.getTitle());
    }

    @Test
    void approveTask_Success() {
        // Arrange
        Long projectId = 1L;
        Long taskId = 1L;
        User owner = new User();
        owner.setId(1L);

        Project project = new Project();
        project.setId(projectId);
        project.setOwner(owner);

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(Task.TaskStatus.COMPLETED_PENDING_APPROVAL);
        project.setTasks(Arrays.asList(task));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        projectService.approveTask(projectId, taskId, owner);

        // Assert
        assertEquals(Task.TaskStatus.COMPLETED, task.getStatus());
        verify(projectRepository).save(project);
        verify(notificationService).createTaskApprovalNotification(task, project);
    }

    @Test
    void createProject_WithLongDescription_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Project project = new Project();
        project.setTitle("Test Project");
        project.setDescription("A".repeat(9000));  // Test with 9,000 characters
        project.setLinkToProject("https://example.com/" + "a".repeat(150));

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.createProject(project, user);

        // Assert
        assertNotNull(result);
        assertEquals(9000, result.getDescription().length());
        assertEquals(user, result.getOwner());
        verify(projectRepository).save(project);
    }
} 