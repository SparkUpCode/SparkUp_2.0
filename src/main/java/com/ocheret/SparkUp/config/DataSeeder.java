package com.ocheret.SparkUp.config;

import com.ocheret.SparkUp.entity.Project;
import com.ocheret.SparkUp.entity.Task;
import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.ProjectRepository;
import com.ocheret.SparkUp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataSeeder(UserRepository userRepository, ProjectRepository projectRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedData() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create admin user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@sparkup.com");
            adminUser.setPassword(passwordEncoder.encode("adminpassword"));
            adminUser.setRoles("ROLE_ADMIN");
            adminUser.setProjects(new ArrayList<>());

            // Create a sample project for admin
            Project adminProject = new Project();
            adminProject.setTitle("Admin Sample Project");
            adminProject.setDescription("This is a sample project created by admin");
            adminProject.setPictures(Arrays.asList(
                "https://example.com/sample1.jpg",
                "https://example.com/sample2.jpg"
            ));
            adminProject.setLinkToProject("https://github.com/admin/sample-project");
            adminProject.setOwner(adminUser);
            adminProject.setTasks(new ArrayList<>());

            // Create sample tasks for the project
            Task task1 = new Task();
            task1.setTitle("Sample Task 1");
            task1.setDescription("This is a sample task for testing");
            task1.setCompleted(false);
            task1.setStatus(Task.TaskStatus.ACTIVE);

            Task task2 = new Task();
            task2.setTitle("Sample Task 2");
            task2.setDescription("Another sample task for testing");
            task2.setCompleted(false);
            task2.setStatus(Task.TaskStatus.ACTIVE);

            adminProject.getTasks().addAll(Arrays.asList(task1, task2));
            adminUser.getProjects().add(adminProject);

            // Save the admin user which will cascade save the project and tasks
            userRepository.save(adminUser);
        }

        // Create a regular test user if it doesn't exist
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("test@sparkup.com");
            testUser.setPassword(passwordEncoder.encode("testpassword"));
            testUser.setRoles("ROLE_USER");
            testUser.setProjects(new ArrayList<>());
            
            userRepository.save(testUser);
        }
    }
}