package com.ocheret.SparkUp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.repository.UserRepository;
import com.ocheret.SparkUp.exception.TaskStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    private final String placeholderImage = "/images/placeholder.webp";

    public Project createProject(Project project, User currentUser) {
        project.setOwner(currentUser);
        List<String> updatedPictures = project.getPictures().stream()
                .map(this::checkAndReplacePicture)
                .collect(Collectors.toList());

        project.setPictures(updatedPictures);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.orElse(null);
    }

    public Project updateProjectFields(Long projectId, Map<String, Object> updates) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (!projectOptional.isPresent()) {
            throw new RuntimeException("Project not found");
        }

        Project project = projectOptional.get();

        if (updates.containsKey("title") && updates.get("title") instanceof String) {
            project.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description") && updates.get("description") instanceof String) {
            project.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("pictures") && updates.get("pictures") instanceof List) {
            List<String> pictures = (List<String>) updates.get("pictures");
            List<String> updatedPictures = pictures.stream()
                    .map(this::checkAndReplacePicture)
                    .collect(Collectors.toList());
            project.setPictures(updatedPictures);
        }
        if (updates.containsKey("tasks") && updates.get("tasks") instanceof Map) {
            Map<String, List<Map<String, Object>>> taskUpdates = (Map<String, List<Map<String, Object>>>) updates.get("tasks");
            updateTasks(project, taskUpdates);
        }

        return projectRepository.save(project);
    }

    private void updateTasks(Project project, Map<String, List<Map<String, Object>>> taskUpdates) {
        if (taskUpdates.containsKey("update")) {
            List<Task> tasksToUpdate = taskUpdates.get("update").stream()
                    .map(this::convertMapToTask)
                    .collect(Collectors.toList());

            project.getTasks().forEach(existingTask -> {
                tasksToUpdate.stream()
                        .filter(newTask -> newTask.getId().equals(existingTask.getId()))
                        .findFirst()
                        .ifPresent(newTask -> {
                            boolean wasCompleted = !existingTask.isCompleted() && newTask.isCompleted();
                            
                            if (wasCompleted) {
                                if (newTask.getCompletionComment() == null || newTask.getCompletionLink() == null) {
                                    throw new IllegalArgumentException("Completion comment and link are required when completing a task");
                                }
                                
                                existingTask.setCompletionComment(newTask.getCompletionComment());
                                existingTask.setCompletionLink(newTask.getCompletionLink());
                                existingTask.setCompletedAt(LocalDateTime.now());
                                existingTask.setStatus(Task.TaskStatus.COMPLETED_PENDING_APPROVAL);
                                
                                // Notify project owner
                                notificationService.createTaskCompletionNotification(existingTask, project, project.getOwner());
                            }
                            
                            existingTask.setTitle(newTask.getTitle());
                            existingTask.setDescription(newTask.getDescription());
                            existingTask.setCompleted(newTask.isCompleted());
                        });
            });
        }
    }

    public void approveTask(Long projectId, Long taskId, User currentUser) {
        Project project = getProjectById(projectId);
        if (project == null) {
            throw new TaskStateException("Project not found");
        }

        // Check if current user is the project owner
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new TaskStateException("Only project owner can approve tasks");
        }

        Task task = project.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new TaskStateException("Task not found"));

        if (task.getStatus() != Task.TaskStatus.COMPLETED_PENDING_APPROVAL) {
            throw new TaskStateException("Task is not pending approval. Current status: " + task.getStatus());
        }

        task.setStatus(Task.TaskStatus.COMPLETED);
        projectRepository.save(project);
        notificationService.createTaskApprovalNotification(task, project);
    }

    public void denyTask(Long projectId, Long taskId, String denialComment) {
        if (denialComment == null || denialComment.trim().isEmpty()) {
            throw new IllegalArgumentException("Denial comment is required");
        }

        Project project = getProjectById(projectId);
        if (project == null) {
            throw new RuntimeException("Project not found");
        }

        Task task = project.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getStatus() != Task.TaskStatus.COMPLETED_PENDING_APPROVAL) {
            throw new IllegalStateException("Task is not pending approval");
        }

        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setCompleted(false);
        task.setDenialComment(denialComment);
        projectRepository.save(project);
        notificationService.createTaskDenialNotification(task, project, denialComment);
    }

    private Long convertToLong(Object id) {
        if (id instanceof Integer) {
            return ((Integer) id).longValue(); // Convert Integer to Long
        } else if (id instanceof Long) {
            return (Long) id;
        } else {
            throw new IllegalArgumentException("Invalid ID type");
        }
    }

    private Task convertMapToTask(Map<String, Object> taskData) {
        return objectMapper.convertValue(taskData, Task.class);
    }

    private String checkAndReplacePicture(String pictureUrl) {
        try {
            URL url = new URL(pictureUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return pictureUrl;
            } else {
                return getPlaceholderImage();
            }
        } catch (IOException e) {
            return getPlaceholderImage();
        }
    }

    private String getPlaceholderImage() {
        try {
            return new ClassPathResource(placeholderImage).getURL().toString();
        } catch (IOException e) {
            throw new RuntimeException("Placeholder image not found");
        }
    }
}