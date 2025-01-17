package com.ocheret.SparkUp.security;

import com.ocheret.SparkUp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);
                return config;
            }))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
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

    // Provide AuthenticationManager for authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}