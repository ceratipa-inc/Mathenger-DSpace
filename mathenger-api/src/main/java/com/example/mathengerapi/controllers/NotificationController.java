package com.example.mathengerapi.controllers;

import com.example.mathengerapi.models.Notification;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
@CrossOrigin
public class NotificationController {

    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getMyTextNotifications(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(notificationService.findTextNotificationsByReceiver(user.getId()), HttpStatus.OK);
    }

}
