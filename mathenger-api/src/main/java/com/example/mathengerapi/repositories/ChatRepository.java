package com.example.mathengerapi.repositories;

import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("select account.chats from Account account where account.id = :accountId")
    List<Chat> findChatsByAccountId(Long accountId);

    // implemented
    List<Message> findOlderMessages(LocalDateTime time, int limit, Long chatId);
}
