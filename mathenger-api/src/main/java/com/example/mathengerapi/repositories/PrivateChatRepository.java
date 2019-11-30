package com.example.mathengerapi.repositories;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, Long> {
    Optional<PrivateChat> findByMembersContainingAndMembersContaining(Account account, Account contact);
}
