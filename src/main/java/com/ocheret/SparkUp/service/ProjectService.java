package com.ocheret.SparkUp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String placeholderImage = "/images/placeholder.webp";

    public Project createProject(Project project) {
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
        if (taskUpdates.containsKey("add")) {
            List<Task> tasksToAdd = taskUpdates.get("add").stream()
                    .map(this::convertMapToTask)
                    .collect(Collectors.toList());
            project.getTasks().addAll(tasksToAdd);  // Add tasks directly to the project’s task list
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

        if (taskUpdates.containsKey("update")) {
            List<Task> tasksToUpdate = taskUpdates.get("update").stream()
                    .map(this::convertMapToTask)
                    .collect(Collectors.toList());

            // Update only the tasks that exist in the project's task list
            project.getTasks().forEach(existingTask -> {
                tasksToUpdate.stream()
                        .filter(newTask -> newTask.getId().equals(existingTask.getId()))
                        .findFirst()
                        .ifPresent(newTask -> {
                            existingTask.setTitle(newTask.getTitle());
                            existingTask.setDescription(newTask.getDescription());
                            existingTask.setCompleted(newTask.isCompleted());
                        });
            });
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
}