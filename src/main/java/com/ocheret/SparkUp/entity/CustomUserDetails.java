package com.ocheret.SparkUp.entity;

import com.ocheret.SparkUp.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert roles (a List<String>) to GrantedAuthority objects
        return user.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> role)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Implement your own logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Implement your own logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Implement your own logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true;  // Implement your own logic if needed
    }

    public User getUser() {
        return user;
    }
}

