package com.example.mathengerapi.integrations.dspace;

import com.example.mathengerapi.dto.ChatsDTO;
import com.example.mathengerapi.events.ChatCreated;
import com.example.mathengerapi.events.ChatDeleted;
import com.example.mathengerapi.events.ChatDetailsUpdated;
import com.example.mathengerapi.events.ChatMemberRemoved;
import com.example.mathengerapi.events.ChatMembersAdded;
import com.example.mathengerapi.integrations.dspace.entity.ChatStatus;
import com.example.mathengerapi.integrations.dspace.model.Collection;
import com.example.mathengerapi.integrations.dspace.repository.ChatStatusRepository;
import com.example.mathengerapi.integrations.dspace.service.ChatStatusService;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.integrations.dspace.service.BotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Async
@Component
@RequiredArgsConstructor
public class DSpaceChatEventsListener {
    private final BotInfoHolder botInfoHolder;
    private final BotMessageService dSpaceMessageService;
    private final ChatStatusService chatStatusService;
    private final TaskScheduler taskScheduler;

    @EventListener
    public void handleChatCreated(ChatCreated event) {
        boolean isActive = botIdIsIn(event.getMemberIds());
        var chatStatus = new ChatStatus(event.getChatId(), event.getName(), event.getChatType(), isActive);
        chatStatusService.save(chatStatus);
        if (isActive) {
            sendHelloMessage(event.getChatId());
        }
    }

    @EventListener
    public void handleChatDetailsUpdated(ChatDetailsUpdated event) {
        chatStatusService.update(event.getChatId(), event.getName());
    }

    @EventListener
    public void handleChatMembersAdded(ChatMembersAdded event) {
        if (event.getNewMemberIds().contains(botInfoHolder.getBotAccount().getId())) {
            chatStatusService.changeActivity(event.getChatId(), true);
            sendHelloMessage(event.getChatId());
        }
    }

    @EventListener
    public void handleChatMemberRemoved(ChatMemberRemoved event) {
        if (Objects.equals(event.getMemberId(), botInfoHolder.getBotAccount().getId())) {
            chatStatusService.changeActivity(event.getChatId(), false);
        }
    }

    @EventListener
    public void handleChatDeleted(ChatDeleted event) {
        chatStatusService.delete(event.getChatId());
    }

    private boolean botIdIsIn(List<Long> memberIds) {
        return memberIds.contains(botInfoHolder.getBotAccount().getId());
    }

    @SneakyThrows
    private void sendHelloMessage(Long chatId) {
        taskScheduler.schedule(
                () -> dSpaceMessageService.sendWelcomeMessage(chatId),
                Instant.now().plusSeconds(2)
        );
    }
}
