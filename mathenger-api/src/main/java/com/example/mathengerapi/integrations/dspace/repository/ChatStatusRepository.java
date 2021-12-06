package com.example.mathengerapi.integrations.dspace.repository;

import com.example.mathengerapi.integrations.dspace.entity.ChatStatus;
import com.example.mathengerapi.models.enums.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatStatusRepository extends JpaRepository<ChatStatus, Long> {
    @Query("select cs.isActive from ChatStatus cs where cs.chatId = :chatId")
    Optional<Boolean> isChatActive(Long chatId);

    @Query("select cs.chatType from ChatStatus cs where cs.chatId = :chatId")
    Optional<ChatType> findChatTypeByChatId(Long chatId);
}
