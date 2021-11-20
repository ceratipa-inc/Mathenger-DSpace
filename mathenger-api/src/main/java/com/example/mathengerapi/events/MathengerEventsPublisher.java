package com.example.mathengerapi.events;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.GroupChat;
import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.models.message.Message;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class MathengerEventsPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @AfterReturning(value = "bean(chatService) && createChatMethods()", returning = "chat")
    public void publishChatCreatedEvent(Chat chat) {
        List<Long> memberIds = chat.getMembers().stream()
                .map(Account::getId)
                .collect(Collectors.toList());

        var eventBuilder = ChatCreated.builder()
                .chatId(chat.getId())
                .chatType(chat.getChatType())
                .memberIds(memberIds);


        if (chat.getChatType().equals(ChatType.GROUP_CHAT)) {
            var groupChat = (GroupChat) chat;
            List<Long> adminIds = groupChat.getAdmins().stream()
                    .map(Account::getId)
                    .collect(Collectors.toList());
            eventBuilder.name(groupChat.getName()).adminIds(adminIds);
        }

        eventPublisher.publishEvent(eventBuilder.build());
    }

    @AfterReturning(value = "bean(chatService) && updateChatDetailsMethod()", returning = "chat")
    public void publishChatDetailsUpdatedEvent(GroupChat chat) {
        var event = ChatDetailsUpdated.builder()
                .chatId(chat.getId())
                .name(chat.getName())
                .build();
        eventPublisher.publishEvent(event);
    }

    @AfterReturning(value = "bean(chatService) && addMembersMethod(newMembers)", returning = "chat", argNames = "chat,newMembers")
    public void publishChatMembersAddedEvent(GroupChat chat, List<Account> newMembers) {
        List<Long> newMemberIds = newMembers.stream().map(Account::getId).collect(Collectors.toList());
        var event = ChatMembersAdded.builder()
                .chatId(chat.getId())
                .newMemberIds(newMemberIds)
                .build();
        eventPublisher.publishEvent(event);
    }

    @AfterReturning(value = "bean(chatService) && removeMemberMethod(member)", returning = "chat", argNames = "chat,member")
    public void publishChatMemberRemovedEvent(GroupChat chat, Account member) {
        var event = ChatMemberRemoved.builder()
                .chatId(chat.getId())
                .memberId(member.getId())
                .build();
        eventPublisher.publishEvent(event);
    }

    @After(value = "bean(chatService) && deleteChatMethod(chat)", argNames = "chat")
    public void publishChatDeletedEvent(Chat chat) {
        var event = ChatDeleted.builder()
                .chatId(chat.getId())
                .build();
        eventPublisher.publishEvent(event);
    }

    @AfterReturning(value = "bean(messageService) && sendMessageMethod()", returning = "message")
    public void publishMessageSentEvent(Message message) {
        var event = MessageSent.builder()
                .messageId(message.getId())
                .authorId(message.getAuthor().getId())
                .senderId(message.getSender().getId())
                .chatId(message.getChat().getId())
                .text(message.getText())
                .time(message.getTime())
                .mathFormula(message.getMathFormula())
                .build();
        eventPublisher.publishEvent(event);
    }

    @Pointcut("execution(* create*Chat(..))")
    private void createChatMethods() {
    }

    @Pointcut("execution(* updateChatDetails(..))")
    private void updateChatDetailsMethod() {
    }

    @Pointcut(value = "execution(* addMembersToChat(..)) && args(.., newMembers)", argNames = "newMembers")
    private void addMembersMethod(List<Account> newMembers) {
    }

    @Pointcut(value = "execution(* removeMemberFromChat(..)) && args(.., member)", argNames = "member")
    private void removeMemberMethod(Account member) {
    }

    @Pointcut(value = "execution(* deleteChat(..)) && args(chat, ..)", argNames = "chat")
    private void deleteChatMethod(Chat chat) {
    }

    @Pointcut(value = "execution(* sendMessage(..))")
    private void sendMessageMethod() {
    }
}
