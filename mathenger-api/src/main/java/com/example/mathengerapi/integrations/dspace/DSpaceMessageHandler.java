package com.example.mathengerapi.integrations.dspace;

import com.example.mathengerapi.events.MessageSent;
import com.example.mathengerapi.integrations.dspace.service.BotCommandsHandler;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.integrations.dspace.service.BotMessageService;
import com.example.mathengerapi.integrations.dspace.service.ChatStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Async
@Component
@RequiredArgsConstructor
public class DSpaceMessageHandler {
    private final ChatStatusService chatStatusService;
    private final BotCommandsHandler botCommandsHandler;
    private final BotInfoHolder botInfoHolder;

    @EventListener
    public void onNewMessage(MessageSent event) {
        if (chatStatusService.isActive(event.getChatId())) {
            handleBotMessage(event);
        }
    }

    private void handleBotMessage(MessageSent event) {
        // TODO refactor to support many different commands without bunch of "if" blocks
        var message = event.getText();
        if (!event.getAuthorId().equals(botInfoHolder.getBotAccount().getId())) {
            if ("/community".equals(message)) {
                botCommandsHandler.handleAllCommunities(event.getChatId());
            } else if (message.matches("/community_(?i)[a-f\\d]{8}-([a-f\\d]{4}-){3}[a-f\\d]{12}")) {
                UUID communityId = UUID.fromString(message.substring(11, message.length()));
                botCommandsHandler.handleAllCollectionsOfCommunity(event.getChatId(), communityId);
            } else if (message.matches("/colpublications_(?i)[a-f\\d]{8}-([a-f\\d]{4}-){3}[a-f\\d]{12}")) {
                UUID collectionId = UUID.fromString(message.substring(17, message.length()));
                botCommandsHandler.handleAllItemsOfCollection(event.getChatId(), collectionId);
            } else if ("/help".equals(message)) {
                botCommandsHandler.handleAllCommands(event.getChatId());
            } else {
                botCommandsHandler.handleIfErrorCommand(event.getChatId(), message);
            }
        }
    }
}
