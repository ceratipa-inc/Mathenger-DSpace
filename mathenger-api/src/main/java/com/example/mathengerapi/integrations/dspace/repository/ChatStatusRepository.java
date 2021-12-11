package com.example.mathengerapi.integrations.dspace.repository;

import com.example.mathengerapi.integrations.dspace.entity.ChatStatus;
import com.example.mathengerapi.models.enums.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.Optional;

@Repository
public interface ChatStatusRepository extends JpaRepository<ChatStatus, Long> {
    @Query("select cs.isActive from ChatStatus cs where cs.chatId = :chatId")
    Optional<Boolean> isChatActive(Long chatId);

    @Query("select cs.chatType from ChatStatus cs where cs.chatId = :chatId")
    Optional<ChatType> findChatTypeByChatId(Long chatId);

    @Modifying
    @Transactional
    @Query("delete from ChatStatus cs where cs.chatId = :chatId ")
    void deleteByChatId(Long chatId);

    @Modifying
    @Transactional
    @Query("update ChatStatus cs set cs.chatName = :name where cs.chatId = :chatId ")
    void updateChatStatus(Long chatId, String name);

    @Modifying
    @Transactional
    @Query("update ChatStatus cs set cs.isActive = :bool where cs.chatId = :chatId")
    void changeActivityOfChatStatus(Long chatId, boolean bool);
}
