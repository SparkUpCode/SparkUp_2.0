package com.ocheret.SparkUp.security;

import com.ocheret.SparkUp.entity.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import org.hibernate.internal.CoreLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        {

            // Get the Authorization header
            final String authorizationHeader = request.getHeader("Authorization");
            System.out.println("here");
            String username = null;
            String jwt = null;

            // Check if the Authorization header contains a Bearer token
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);  // Remove "Bearer " prefix
                System.out.println("token: " + jwt);


                try {
                    // Extract username from the token
                    username = jwtUtil.extractUsername(jwt);
                    System.out.println("username" + username);
                } catch (JwtException e) {
                    // Log or handle the exception if the JWT is invalid
                    System.out.println("Invalid JWT token: " + e.getMessage());
                }
            }

            // Validate the JWT and the user is not authenticated yet
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // If the token is valid, authenticate the user
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    System.out.println("token validating ... ");
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            // Continue with the filter chain
            filterChain.doFilter(request, response);
        }

    }
}
