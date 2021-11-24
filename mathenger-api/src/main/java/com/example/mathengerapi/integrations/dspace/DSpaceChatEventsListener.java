package com.example.mathengerapi.integrations.dspace;

import com.example.mathengerapi.events.ChatCreated;
import com.example.mathengerapi.events.ChatDeleted;
import com.example.mathengerapi.events.ChatDetailsUpdated;
import com.example.mathengerapi.events.ChatMemberRemoved;
import com.example.mathengerapi.events.ChatMembersAdded;
import com.example.mathengerapi.integrations.dspace.entity.ChatStatus;
import com.example.mathengerapi.integrations.dspace.service.ChatStatusService;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.integrations.dspace.service.BotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

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
        // TODO change chat name in db
    }

    @EventListener
    public void handleChatMembersAdded(ChatMembersAdded event) {
        // TODO if newMemberIds contains botId then activate chat and send hello message
    }

    @EventListener
    public void handleChatMemberRemoved(ChatMemberRemoved event) {
        // TODO if memberId == botId then deactivate chat
    }

    @EventListener
    public void handleChatDeleted(ChatDeleted event) {
        // TODO delete chat from db
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
