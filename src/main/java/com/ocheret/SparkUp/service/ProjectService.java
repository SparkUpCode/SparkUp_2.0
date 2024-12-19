package com.ocheret.SparkUp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.enums.TaskStatus;
import com.ocheret.SparkUp.exception.UnauthorizedAccessException;
import com.ocheret.SparkUp.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String placeholderImage = "/images/placeholder.webp";

    public Project createProject(Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Authentication: {}", authentication);
        logger.debug("Principal: {}", authentication != null ? authentication.getPrincipal() : "null");
        logger.debug("Authorities: {}", authentication != null ? authentication.getAuthorities() : "null");
        
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new SecurityException("User must be authenticated to create a project");
        }
        
        String username = authentication.getName();
        project.setCreatorUsername(username);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedAccessException("User must be authenticated to update a project");
        }
        String currentUsername = authentication.getName();

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (!projectOptional.isPresent()) {
            throw new RuntimeException("Project not found");
        }

        Project project = projectOptional.get();
        
        if (!project.getCreatorUsername().equals(currentUsername)) {
            throw new UnauthorizedAccessException("Only the project creator can update this project");
        }

        if (updates.containsKey("title") && updates.get("title") instanceof String) {
            project.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description") && updates.get("description") instanceof String) {
            project.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("stage") && updates.get("stage") instanceof String) {
            project.setStage((String) updates.get("stage"));
        }
        if (updates.containsKey("industry") && updates.get("industry") instanceof String) {
            project.setIndustry((String) updates.get("industry"));
        }
        if (updates.containsKey("linkToProject") && updates.get("linkToProject") instanceof String) {
            project.setLinkToProject((String) updates.get("linkToProject"));
        }
        if (updates.containsKey("pictures") && updates.get("pictures") instanceof List) {
            List<String> pictures = (List<String>) updates.get("pictures");
            List<String> updatedPictures = pictures.stream()
                    .map(this::checkAndReplacePicture)
                    .collect(Collectors.toList());
            project.setPictures(updatedPictures);
        }
        if (updates.containsKey("stageData") && updates.get("stageData") instanceof Map) {
            Map<String, String> stageData = (Map<String, String>) updates.get("stageData");
            project.setStageData(stageData);
        }
        if (updates.containsKey("tasks") && updates.get("tasks") instanceof Map) {
            Map<String, List<Map<String, Object>>> taskUpdates = 
                (Map<String, List<Map<String, Object>>>) updates.get("tasks");
            updateTasks(project, taskUpdates);
        }
        if (updates.containsKey("problem") && updates.get("problem") instanceof String) {
            project.setProblem((String) updates.get("problem"));
        }
        if (updates.containsKey("solution") && updates.get("solution") instanceof String) {
            project.setSolution((String) updates.get("solution"));
        }
        if (updates.containsKey("prototype") && updates.get("prototype") instanceof String) {
            project.setPrototype((String) updates.get("prototype"));
        }
        if (updates.containsKey("idealCustomer") && updates.get("idealCustomer") instanceof String) {
            project.setIdealCustomer((String) updates.get("idealCustomer"));
        }
        if (updates.containsKey("competitors") && updates.get("competitors") instanceof String) {
            project.setCompetitors((String) updates.get("competitors"));
        }
        if (updates.containsKey("goals") && updates.get("goals") instanceof String) {
            project.setGoals((String) updates.get("goals"));
        }

        return projectRepository.save(project);
    }

    private void updateTasks(Project project, Map<String, List<Map<String, Object>>> taskUpdates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if (taskUpdates.containsKey("add")) {
            List<Task> tasksToAdd = taskUpdates.get("add").stream()
                    .map(this::convertMapToTask)
                    .peek(task -> {
                        task.setStatus(TaskStatus.OPEN);
                        task.setAssignedUsername(null);
                    })
                    .collect(Collectors.toList());
            project.getTasks().addAll(tasksToAdd);
        }

        if (taskUpdates.containsKey("update")) {
            List<Task> tasksToUpdate = taskUpdates.get("update").stream()
                    .map(this::convertMapToTask)
                    .collect(Collectors.toList());

            project.getTasks().forEach(existingTask -> {
                tasksToUpdate.stream()
                        .filter(newTask -> newTask.getId().equals(existingTask.getId()))
                        .findFirst()
                        .ifPresent(newTask -> {
                            // Only allow updates if user is creator or assigned to the task
                            if (project.getCreatorUsername().equals(currentUsername) || 
                                currentUsername.equals(existingTask.getAssignedUsername())) {
                                existingTask.setTitle(newTask.getTitle());
                                existingTask.setDescription(newTask.getDescription());
                                existingTask.setComment(newTask.getComment());
                                existingTask.setPullRequestUrl(newTask.getPullRequestUrl());
                                
                                // Handle status changes
                               
                               
                                if (newTask.getStatus() == TaskStatus.PENDING_APPROVAL) {
                                    existingTask.setStatus(TaskStatus.PENDING_APPROVAL);
                                }

                                if (existingTask.getStatus() == TaskStatus.PENDING_APPROVAL && 
                                    newTask.isCompleted()) {
                                    existingTask.setCompleted(true);
                                    existingTask.setStatus(TaskStatus.COMPLETED);
                                }
                                
                                // New denial logic
                                if (existingTask.getStatus() == TaskStatus.PENDING_APPROVAL && 
                                    newTask.getStatus() == TaskStatus.ACTIVE) {
                                    existingTask.setStatus(TaskStatus.ACTIVE);
                                    // Optionally, you might want to clear or update other fields
                                    existingTask.setComment("Task denied by project owner");
                                }
                                
                                // Only project creator can complete tasks
                                if (project.getCreatorUsername().equals(currentUsername) && 
                                    existingTask.getStatus() == TaskStatus.PENDING_APPROVAL &&
                                    newTask.isCompleted()) {
                                    existingTask.setCompleted(true);
                                    existingTask.setStatus(TaskStatus.COMPLETED);
                                }
                            }
                        });
            });
        }

        if (taskUpdates.containsKey("remove")) {
            // Convert the task IDs to remove into Longs
            List<Long> idsToRemove = taskUpdates.get("remove").stream()
                    .map(map -> convertToLong(map.get("id")))
                    .collect(Collectors.toList());

            // Filter the project’s tasks, keeping only those not in the idsToRemove list
            // and ensuring only tasks that belong to the project are removed
            project.getTasks().removeIf(task -> idsToRemove.contains(task.getId()));
        }
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

    public Task assignTask(Long projectId, Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedAccessException("User must be authenticated to pick up a task");
        }
        String username = authentication.getName();

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (!projectOptional.isPresent()) {
            throw new RuntimeException("Project not found");
        }

        Project project = projectOptional.get();
        Task taskToAssign = project.getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found in project"));

        if (taskToAssign.getStatus() != TaskStatus.OPEN) {
            throw new RuntimeException("Task is not available for assignment");
        }

        taskToAssign.setStatus(TaskStatus.ACTIVE);
        taskToAssign.setAssignedUsername(username);

        return projectRepository.save(project).getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found after save"));
    }
}