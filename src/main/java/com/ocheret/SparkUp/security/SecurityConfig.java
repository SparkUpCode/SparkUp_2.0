package com.ocheret.SparkUp.security;


import com.ocheret.SparkUp.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disables authentication and CSRF for all endpoints
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()); // Allow all requests without authentication
        return http.build();
    }

    // Configure Argon2PasswordEncoder as the password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Configure Argon2 with custom parameters
        int saltLength = 16;  // 16 bytes for salt
        int hashLength = 32;  // 32 bytes for the resulting hash
        int parallelism = 1;  // Use 1 thread
        int memoryCost = 4096;  // 4MB of memory
        int iterations = 3;  // Number of iterations

        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memoryCost, iterations);
    }

//
//    // Define the security filter chain configuration
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())  // Disable CSRF for API requests
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()  // Permit login and register
//                        .anyRequest().authenticated()  // All other requests require authentication
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // Use stateless session for JWT
//
//        return http.build();
//    }
//
//    // Provide AuthenticationManager for authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}