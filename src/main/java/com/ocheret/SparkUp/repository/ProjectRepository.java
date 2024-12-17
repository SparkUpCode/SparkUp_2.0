package com.ocheret.SparkUp.repository;

import com.ocheret.SparkUp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCreatorUsername(String creatorUsername);
}