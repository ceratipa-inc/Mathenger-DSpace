package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.Message;
import com.example.mathengerapi.models.Notification;
import com.example.mathengerapi.models.enums.NotificationType;
import com.example.mathengerapi.repositories.AccountRepository;
import com.example.mathengerapi.repositories.ChatRepository;
import com.example.mathengerapi.repositories.MessageRepository;
import com.example.mathengerapi.repositories.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;
    private ChatRepository chatRepository;
    private AccountRepository accountRepository;
    private NotificationRepository notificationRepository;
    private SimpMessagingTemplate messagingTemplate;
    private ObjectMapper objectMapper;

    @Transactional
    public Message sendMessage(Long userId, Message message, Long chatId) throws JsonProcessingException {
        var sender = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        var chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found!"));
        if (!sender.getChats().contains(chat)) {
            throw new IllegalArgumentException("You are not member of this chat!");
        }
        message.setAuthor(sender);
        message.setSender(sender);
        message.setTime(LocalDateTime.now());
        message = messageRepository.save(message);
        chat.getMessages().add(message);
        chatRepository.save(chat);

        messagingTemplate.convertAndSend("/topic/chat/" + chat.getId(),
                objectMapper.writeValueAsString(message));

        var membersToNotify = accountRepository.findByChatsNotContainingAndIdIsIn(chat,
                chat.getMembers().stream()
                        .map(Account::getId)
                        .collect(Collectors.toList()));

        membersToNotify.stream().forEach(member -> {
            member.getChats().add(chat);
        });

        var notifications = notificationRepository.saveAll(
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
                        .collect(Collectors.toList()));

        for (Notification notification : notifications) {
            messagingTemplate
                    .convertAndSend("/topic/user/" + notification.getReceiver().getId() + "/notifications",
                            objectMapper.writeValueAsString(notification));
        }

        return message;
    }
}
