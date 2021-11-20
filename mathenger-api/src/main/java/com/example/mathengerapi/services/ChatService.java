package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.GroupChat;
import com.example.mathengerapi.models.PrivateChat;
import com.example.mathengerapi.models.message.Message;
import com.example.mathengerapi.repositories.AccountRepository;
import com.example.mathengerapi.repositories.ChatRepository;
import com.example.mathengerapi.repositories.GroupChatRepository;
import com.example.mathengerapi.repositories.PrivateChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {
    private ChatRepository chatRepository;
    private PrivateChatRepository privateChatRepository;
    private GroupChatRepository groupChatRepository;
    private AccountRepository accountRepository;
    private ColorProvider colorProvider;
    private MessageService messageService;
    private NotificationService notificationService;

    public List<Chat> findByUserId(Long userId) {
        return chatRepository.findChatsByAccountId(userId);
    }

    public Optional<PrivateChat> findPrivateChatWithMembers(Account contact, Account account) {
        return privateChatRepository.findByMembersContainingAndMembersContaining(account, contact);
    }

    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    public PrivateChat createPrivateChat(Account account, Account account2) {
        var chat = new PrivateChat();
        chat.setMembers(new LinkedList<>());
        chat.setMessages(new LinkedList<>());
        chat.getMembers().add(account);
        chat.getMembers().add(account2);
        return privateChatRepository.save(chat);
    }

    public GroupChat createGroupChat(Long userId, GroupChat chat) throws JsonProcessingException {
        var creator = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        var contacts = creator.getContacts();
        var members = accountRepository.findByIdIsIn(
                chat.getMembers()
                        .stream().map(Account::getId).collect(Collectors.toList()));
        if (!contacts.containsAll(members))
            throw new IllegalArgumentException("Only contacts can be added to chat");
        members.add(creator);
        chat.setMembers(new ArrayList<>(members));
        chat.setAdmins(new ArrayList<>(Collections.singletonList(creator)));
        chat.setColor(colorProvider.getRandomColor());
        chat.setCreator(creator);
        chat.setMessages(new LinkedList<>());
        chat = groupChatRepository.save(chat);
        if (!creator.getChats().contains(chat)) {
            creator.getChats().add(chat);
            accountRepository.save(creator);
        }
        var creationMessage = new Message(0L, creator, creator, LocalDateTime.now(),
                creator.getFirstName() + " " + creator.getLastName() + " started chat " + chat.getName(),
                null, chat);
        messageService.sendMessage(creator.getId(), creationMessage, chat.getId());
        return chat;
    }

    public GroupChat updateChatDetails(Long userId, GroupChat chat, GroupChat newChat) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getAdmins().contains(account))
            throw new IllegalArgumentException("You are not authorized to edit this chat");
        chat.update(newChat);
        var updatedChat = groupChatRepository.save(chat);
        notificationService.notifyChatUpdate(updatedChat, account);
        return updatedChat;
    }

    public GroupChat addMembersToChat(GroupChat chat, List<Account> newMembers) {
        chat.getMembers().addAll(newMembers);
        return groupChatRepository.save(chat);
    }

    public GroupChat removeMemberFromChat(GroupChat chat, Account member) {
        chat.getMembers().remove(member);
        return groupChatRepository.save(chat);
    }

    public GroupChat addAdminToChat(GroupChat chat, Account member) {
        chat.getAdmins().add(member);
        return groupChatRepository.save(chat);
    }

    public GroupChat removeAdminFromChat(GroupChat chat, Account member) {
        chat.getAdmins().remove(member);
        return groupChatRepository.save(chat);
    }

    public void deleteChat(Chat chat) {
        chatRepository.delete(chat);
    }
}
