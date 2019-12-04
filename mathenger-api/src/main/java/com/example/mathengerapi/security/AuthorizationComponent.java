package com.example.mathengerapi.security;

import com.example.mathengerapi.models.User;
import com.example.mathengerapi.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component("Auth")
@AllArgsConstructor
public class AuthorizationComponent {

    private NotificationRepository notificationRepository;

    public boolean mayDeleteNotification(@NonNull final User principal, @NonNull final Long notificationId) {
        return notificationRepository.existsByReceiverIdAndId(principal.getId(), notificationId);
    }
}
