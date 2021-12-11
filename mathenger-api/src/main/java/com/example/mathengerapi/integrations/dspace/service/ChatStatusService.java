package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.integrations.dspace.entity.ChatStatus;
import com.example.mathengerapi.integrations.dspace.repository.ChatStatusRepository;
import com.example.mathengerapi.models.enums.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatStatusService {
    private final ChatStatusRepository chatStatusRepository;

    public void save(ChatStatus chatStatus) {
        chatStatusRepository.save(chatStatus);
    }

    public boolean isActive(Long chatId) {
        return chatStatusRepository.isChatActive(chatId).orElse(false);
    }

    public boolean isPrivateChat(Long chatId) {
        return chatStatusRepository
                .findChatTypeByChatId(chatId)
                .map(type -> type.equals(ChatType.PRIVATE_CHAT))
                .orElse(false);
    }

    public void delete(Long chatId) {
        chatStatusRepository.deleteByChatId(chatId);
    }

    public void update(Long chatId, String name) {
        chatStatusRepository.updateChatStatus(chatId, name);
    }

    public void changeActivity(Long chatId, boolean bool) {
        chatStatusRepository.changeActivityOfChatStatus(chatId, bool);
    }

}
