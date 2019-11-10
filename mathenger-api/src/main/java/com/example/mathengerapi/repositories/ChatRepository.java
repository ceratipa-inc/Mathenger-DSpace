package com.example.mathengerapi.repositories;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.enums.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("select account.chats from Account account where account.id = :accountId")
    List<Chat> findChatsByAccountId(Long accountId);
    Optional<Chat> findByChatTypeAndMembersContainingAndMembersContaining(ChatType type, Account account, Account contact);
}
