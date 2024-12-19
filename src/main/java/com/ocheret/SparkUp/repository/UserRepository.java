package com.ocheret.SparkUp.repository;

import com.ocheret.SparkUp.entity.Task;

import com.ocheret.SparkUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT DISTINCT t FROM Project p JOIN p.tasks t WHERE t.assignedUsername = :username")
    List<Task> findAssignedTasksByUsername(String username);
}
