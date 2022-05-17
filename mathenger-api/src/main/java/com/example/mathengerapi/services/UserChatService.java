package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.GroupChat;
import com.example.mathengerapi.models.enums.AccountType;
import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.models.message.Message;
import com.example.mathengerapi.repositories.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserChatService {
    private final AccountRepository accountRepository;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final ChatService chatService;

    public Chat startPrivateChat(Long userId, Account contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Account with such id not found!");
        }
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!account.getContacts().contains(contact))
            throw new IllegalArgumentException("Can't start chatting with user! Add him as contact first.");
        var chat = chatService.findPrivateChatWithMembers(contact, account)
                .orElseGet(() -> chatService.createPrivateChat(account, contact));
        if (!account.getChats().contains(chat)) {
            account.getChats().add(chat);
            accountRepository.save(account);
        }
        return chat;
    }

    public GroupChat addMembers(Long userId, GroupChat chat, List<Account> newMembers) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getMembers().contains(account))
            throw new IllegalArgumentException("You are not member of this chat");
        newMembers = accountRepository.findByIdIsIn(
                        newMembers.stream()
                                .map(Account::getId)
                                .collect(Collectors.toList())
                )
                .stream().filter(member -> !chat.getMembers().contains(member))
                .filter(member -> account.getContacts().contains(member))
                .collect(Collectors.toList());
        GroupChat updatedChat = chatService.addMembersToChat(chat, newMembers);
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
        member.getChats().remove(chat);
        accountRepository.save(member);
        notificationService.notifyRemovalFromChat(chat, account, member);
        GroupChat updatedChat = chatService.removeMemberFromChat(chat, member);
        var messageText = String.format("%s %s removed %s %s from the chat",
                account.getFirstName(), account.getLastName(),
                member.getFirstName(), member.getLastName());
        var message = new Message(0L, account, account, LocalDateTime.now(), messageText, null, updatedChat);
        messageService.sendMessage(userId, message, updatedChat.getId());
        notificationService.notifyChatUpdate(updatedChat, account);
        return updatedChat;
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
        GroupChat updatedChat = chatService.addAdminToChat(chat, member);
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
        GroupChat updatedChat = chatService.removeAdminFromChat(chat, member);
        notificationService.notifyChatUpdate(updatedChat, account);
        return updatedChat;
    }

    public void deleteChatFromList(Long userId, Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("Chat not found!");
        }
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        account.getChats().remove(chat);
        if (chat.getChatType().equals(ChatType.GROUP_CHAT) && isNoMembersLeft(chat)) {
            chatService.deleteChat(chat);
        }
        accountRepository.save(account);
    }

    public void leaveGroupChat(Long userId, GroupChat chat) throws JsonProcessingException {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        if (!chat.getMembers().contains(account))
            throw new IllegalArgumentException("You are not member of the chat");
        var leaveMessage = new Message(0L, account, account, LocalDateTime.now(),
                account.getFirstName() + " " + account.getLastName() + " has left the chat!", null, chat);
        messageService.sendMessage(userId, leaveMessage, chat.getId());
        chat.getMembers().remove(account);
        account.getChats().remove(chat);
        if (isNoMembersLeft(chat)) {
            chatService.deleteChat(chat);
        } else {
            chatService.save(chat);
            notificationService.notifyChatUpdate(chat, account);
        }
        accountRepository.save(account);
    }

    private boolean isNoMembersLeft(Chat chat) {
        return chat.getMembers().stream()
                .filter(member -> !AccountType.SYSTEM.equals(member.getAccountType()))
                .noneMatch(member -> member.getChats().contains(chat));
    }
}
