package com.ocheret.SparkUp.service;

import com.ocheret.SparkUp.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Project createProject(Project project);
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Long id);
    Project updateProject(Long id, Project updatedProject);
    void deleteProject(Long id);
}
