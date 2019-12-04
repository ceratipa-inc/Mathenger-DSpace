package com.example.mathengerapi.controllers;

import com.example.mathengerapi.models.Notification;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @Auth.mayDeleteNotification(principal, #id)")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
