package com.example.mathengerapi.repositories;

import com.example.mathengerapi.models.Notification;
import com.example.mathengerapi.models.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdAndType(Long receiverId, NotificationType type);
}
