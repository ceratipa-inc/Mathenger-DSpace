package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.GroupChat;
import com.example.mathengerapi.models.Notification;
import com.example.mathengerapi.models.enums.NotificationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
    private SimpMessagingTemplate messagingTemplate;
    private ObjectMapper objectMapper;

    public void notifyChatUpdate(GroupChat chat, Account sender) throws JsonProcessingException {
        var notification = new Notification(0L, NotificationType.CHAT_UPDATE, chat.getCreator(), sender,
                objectMapper.writeValueAsString(chat));
        for (Account member : chat.getMembers()) {
            messagingTemplate.convertAndSend("/topic/user/" + member.getId() + "/notifications",
                    objectMapper.writeValueAsString(notification));
        }
    }

    public void notifyRemovalFromChat(Long chatId, Account producer, Account receiver) throws JsonProcessingException {
        var unsubscribeNotification = new Notification(0L, NotificationType.CHAT_UNSUBSCRIBE,
                receiver, producer, chatId.toString());
        messagingTemplate.convertAndSend("/topic/user/" + receiver.getId() + "/notifications",
                objectMapper.writeValueAsString(unsubscribeNotification));
    }
}
