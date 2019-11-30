package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.GroupChat;
import com.example.mathengerapi.models.Notification;
import com.example.mathengerapi.models.enums.NotificationType;
import com.example.mathengerapi.repositories.AccountRepository;
import com.example.mathengerapi.repositories.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {
    private SimpMessagingTemplate messagingTemplate;
    private ObjectMapper objectMapper;
    private AccountRepository accountRepository;
    private NotificationRepository notificationRepository;

    public void notifyChatUpdate(GroupChat chat, Account sender) throws JsonProcessingException {
        var notification = new Notification(0L, NotificationType.CHAT_UPDATE, chat.getCreator(), sender,
                objectMapper.writeValueAsString(chat));
        for (Account member : chat.getMembers()) {
            messagingTemplate.convertAndSend("/topic/user/" + member.getId() + "/notifications",
                    objectMapper.writeValueAsString(notification));
        }
    }

    public void notifyRemovalFromChat(GroupChat chat, Account producer, Account receiver) throws JsonProcessingException {
        var unsubscribeNotification = new Notification(0L, NotificationType.CHAT_UNSUBSCRIBE,
                receiver, producer, chat.getId().toString());
        var text = String.format("%s removed you from \" %s \"!",
                producer.getFirstName() + " " + producer.getLastName(), chat.getName());
        var textNotification = notificationRepository.save(
                new Notification(0L, NotificationType.TEXT, receiver, producer, text));
        messagingTemplate.convertAndSend("/topic/user/" + receiver.getId() + "/notifications",
                objectMapper.writeValueAsString(unsubscribeNotification));
        messagingTemplate.convertAndSend("/topic/user/" + receiver.getId() + "/notifications",
                objectMapper.writeValueAsString(textNotification));
    }

    public void remindMembersAboutChat(Account sender, Chat chat) throws JsonProcessingException {
        var membersToNotify = accountRepository.findByChatsNotContainingAndIdIsIn(chat,
                chat.getMembers().stream()
                        .map(Account::getId)
                        .collect(Collectors.toList()));

        membersToNotify.forEach(member -> member.getChats().add(chat));

        var notifications =
                membersToNotify.stream()
                        .map(member -> {
                            try {
                                return new Notification(0L, NotificationType.NEW_CHAT,
                                        member, sender, objectMapper.writeValueAsString(chat));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());

        for (Notification notification : notifications) {
            messagingTemplate
                    .convertAndSend("/topic/user/" + notification.getReceiver().getId() + "/notifications",
                            objectMapper.writeValueAsString(notification));
        }
    }

    public List<Notification> findTextNotificationsByReceiver(Long userId) {
        return notificationRepository.findByReceiverIdAndType(userId, NotificationType.TEXT);
    }
}
