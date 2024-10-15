package com.ocheret.SparkUp.config;

import com.ocheret.SparkUp.entity.User;
import com.ocheret.SparkUp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Use PasswordEncoder to apply Argon2

    @Autowired
    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedData() {
        // Check if admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create an admin user with Argon2-hashed password
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("adminpassword"));  // Use PasswordEncoder for Argon2
            adminUser.setRoles("ROLE_ADMIN");

            userRepository.save(adminUser);
        }

        // You can seed other users as needed
    }
}