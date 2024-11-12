package com.ocheret.SparkUp.repository;

import com.ocheret.SparkUp.entity.Notification;
import com.ocheret.SparkUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    List<Notification> findByUserAndReadOrderByCreatedAtDesc(User user, boolean read);
} 