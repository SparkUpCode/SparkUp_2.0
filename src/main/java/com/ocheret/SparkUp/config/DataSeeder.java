package com.ocheret.SparkUp.config;

//import com.ocheret.SparkUp.entity.User;
//import com.ocheret.SparkUp.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import jakarta.annotation.PostConstruct;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//public class DataSeeder {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    public void seedDatabase() {
//        try {
//            seedUsers();
//        } catch (Exception e) {
//            // Log the exception details for debugging
//            System.err.println("Error seeding database: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void seedUsers() {
//        // Check if the user already exists to prevent duplicate entries
//        Optional<User> existingAdmin = userRepository.findByUsername("admin");
//
//        if (existingAdmin.isEmpty()) {
//            // If the user does not exist, create a new user
//            User adminUser = new User();
//            adminUser.setUsername("admin");
//            adminUser.setEmail("admin@example.com");
//
//            // Encode the password using the PasswordEncoder bean
//            adminUser.setPassword(passwordEncoder.encode("adminpassword"));
//
//            // Set roles (adjust based on how you store roles in your User entity)
//            List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER");
//            adminUser.setRoles(roles);
//
//            // Save the user
//            userRepository.save(adminUser);
//            System.out.println("Admin user created.");
//        } else {
//            System.out.println("Admin user already exists.");
//        }
//    }
//}