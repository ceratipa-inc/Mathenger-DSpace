package com.example.mathengerapi.repositories;

import com.example.mathengerapi.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
