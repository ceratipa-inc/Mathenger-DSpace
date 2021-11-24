package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.integrations.dspace.entity.ChatStatus;
import com.example.mathengerapi.integrations.dspace.repository.ChatStatusRepository;
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
}
