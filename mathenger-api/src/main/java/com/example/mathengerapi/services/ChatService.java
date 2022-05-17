package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.GroupChat;
import com.example.mathengerapi.models.PrivateChat;
import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.models.message.Message;
import com.example.mathengerapi.repositories.AccountRepository;
import com.example.mathengerapi.repositories.ChatRepository;
import com.example.mathengerapi.repositories.GroupChatRepository;
import com.example.mathengerapi.repositories.PrivateChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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

    public Chat startPrivateChat(Long userId, Account contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Account with such id not found!");
        }
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!account.getContacts().contains(contact))
            throw new IllegalArgumentException("Can't start chatting with user! Add him as contact first.");
        var chat = privateChatRepository.findByMembersContainingAndMembersContaining(account, contact)
                .or(() -> Optional.of(newChatWithMembers(account, contact))).get();
        if (!account.getChats().contains(chat)) {
            account.getChats().add(chat);
            accountRepository.save(account);
        }
        return chat;
    }

    private PrivateChat newChatWithMembers(Account account, Account account2) {
        var chat = new PrivateChat();
        chat.setMembers(new LinkedList<>());
        chat.setMessages(new LinkedList<>());
        chat.getMembers().add(account);
        chat.getMembers().add(account2);
        return privateChatRepository.save(chat);
    }

    public void delete(Long userId, Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("Chat not found!");
        }
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        account.getChats().remove(chat);
        // delete chat from DB if all members removed it from their lists
        if (chat.getChatType().equals(ChatType.GROUP_CHAT)) {
            chat.getMembers().stream()
                    .filter(member -> member.getChats().contains(chat))
                    .findFirst().ifPresentOrElse(member -> {
                    },
                    () -> chatRepository.delete(chat));
        }
        accountRepository.save(account);
    }

    public GroupChat startGroupChat(Long userId, GroupChat chat) throws JsonProcessingException {
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

    public GroupChat addAdmin(Long userId, GroupChat chat, Account member) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getAdmins().contains(account))
            throw new IllegalArgumentException("You are not an administrator of this chat");
        if (!chat.getMembers().contains(member))
            throw new IllegalArgumentException("User is not a member of chat");
        if (chat.getAdmins().contains(member))
            throw new IllegalArgumentException("Member is already an administrator");
        chat.getAdmins().add(member);
        var updatedChat = groupChatRepository.save(chat);
        notificationService.notifyChatUpdate(updatedChat, account);
        return updatedChat;
    }

    public GroupChat removeAdmin(Long userId, GroupChat chat, Account member) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getCreator().equals(account))
            throw new IllegalArgumentException("You are not creator of this chat!");
        if (!chat.getMembers().contains(member))
            throw new IllegalArgumentException("User is not a member of chat");
        if (!chat.getAdmins().contains(member))
            throw new IllegalArgumentException("Member is not an administrator");
        chat.getAdmins().remove(member);
        var updatedChat = groupChatRepository.save(chat);
        notificationService.notifyChatUpdate(updatedChat, account);
        return updatedChat;
    }

    public GroupChat addMembers(Long userId, GroupChat chat, List<Account> newMembers) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getMembers().contains(account))
            throw new IllegalArgumentException("You are not member of this chat");
        newMembers = accountRepository.findByIdIsIn(newMembers
                .stream().map(Account::getId).collect(Collectors.toList()))
                .stream().filter(member -> !chat.getMembers().contains(member))
                .filter(member -> account.getContacts().contains(member))
                .collect(Collectors.toList());
        chat.getMembers().addAll(newMembers);
        var updatedChat = groupChatRepository.save(chat);
        if (!newMembers.isEmpty()) {
            var messageText = newMembers.size() > 1
                    ? String.format("%s %s added %d members to the chat",
                    account.getFirstName(), account.getLastName(), newMembers.size())
                    : String.format("%s %s added %s %s to the chat",
                    account.getFirstName(), account.getLastName(), newMembers.get(0).getFirstName(),
                    newMembers.get(0).getLastName());
            var message = new Message(0L, account, account, LocalDateTime.now(), messageText, null, chat);
            messageService.sendMessage(userId, message, updatedChat.getId());
            notificationService.notifyChatUpdate(updatedChat, account);
        }
        return updatedChat;
    }

    public GroupChat removeMember(Long userId, GroupChat chat, Account member) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getMembers().contains(member))
            throw new IllegalArgumentException("This user is not member of the chat");
        if ((chat.getAdmins().contains(member) && !chat.getCreator().equals(account))
                || !chat.getAdmins().contains(account))
            throw new IllegalArgumentException("You are not authorized to remove this member");
        chat.getMembers().remove(member);
        member.getChats().remove(chat);
        accountRepository.save(member);
        notificationService.notifyRemovalFromChat(chat, account, member);
        var updatedChat = groupChatRepository.save(chat);
        var messageText = String.format("%s %s removed %s %s from the chat",
                account.getFirstName(), account.getLastName(),
                member.getFirstName(), member.getLastName());
        var message = new Message(0L, account, account, LocalDateTime.now(), messageText, null, updatedChat);
        messageService.sendMessage(userId, message, updatedChat.getId());
        notificationService.notifyChatUpdate(updatedChat, account);
        return updatedChat;
    }

    public GroupChat updateGroupChat(Long userId, GroupChat chat, GroupChat newChat) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getAdmins().contains(account))
            throw new IllegalArgumentException("You are not authorized to edit this chat");
        chat.update(newChat);
        var updatedChat = groupChatRepository.save(chat);
        notificationService.notifyChatUpdate(updatedChat, account);
        return updatedChat;
    }

    public void leave(Long userId, GroupChat chat) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getMembers().contains(account))
            throw new IllegalArgumentException("You are not member of the chat");
        var leaveMessage = new Message(0L, account, account, LocalDateTime.now(),
                account.getFirstName() + " " + account.getLastName() + " has left the chat!", null, chat);
        messageService.sendMessage(userId, leaveMessage, chat.getId());
        chat.getMembers().remove(account);
        account.getChats().remove(chat);
        groupChatRepository.save(chat);
        accountRepository.save(account);
        notificationService.notifyChatUpdate(chat, account);
    }
}
