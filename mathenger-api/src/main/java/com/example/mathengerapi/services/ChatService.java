package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.ChatType;
import com.example.mathengerapi.repositories.AccountRepository;
import com.example.mathengerapi.repositories.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private ChatRepository chatRepository;
    private AccountRepository accountRepository;
    private ColorProvider colorProvider;

    public List<Chat> findByUserId(Long userId) {
        return chatRepository.findChatsByAccountId(userId);
    }

    public Chat startPrivateChat(Long userId, Account contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Account with such id not found!");
        }
        var account = accountRepository.findById(userId).get();
        if (!account.getContacts().contains(contact))
            throw new IllegalArgumentException("Can't start chatting with user! Add him as contact first.");
        var chat = chatRepository
                .findByChatTypeAndMembersContainingAndMembersContaining(ChatType.PRIVATE_CHAT, account, contact)
                .or(() -> Optional.ofNullable(newChatWithMembers(account, contact))).get();
        if (!account.getChats().contains(chat)) account.getChats().add(chat);
        accountRepository.save(account);
        return chat;
    }

    private Chat newChatWithMembers(Account account, Account account2) {
        var chat = new Chat(0L, new LinkedHashSet<>(), new LinkedList<>());
        chat.getMembers().add(account);
        chat.getMembers().add(account2);
        return chatRepository.save(chat);
    }

    public void delete(Long userId, Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("Chat not found!");
        }
        var account = accountRepository.findById(userId).get();
        account.getChats().remove(chat);
        // delete chat from DB if all members removed it from their lists
        if (chat.getChatType().equals(ChatType.GROUP_CHAT)) {
            chat.getMembers().stream()
                    .filter(member -> member.getChats().contains(chat))
                    .findFirst().ifPresentOrElse(null,
                    () -> chatRepository.delete(chat));
        }
        accountRepository.save(account);
    }
}
