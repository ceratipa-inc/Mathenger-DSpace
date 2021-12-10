package com.example.mathengerapi.integrations.dspace;

import com.example.mathengerapi.events.MessageSent;
import com.example.mathengerapi.integrations.dspace.service.BotCommandsHandler;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.integrations.dspace.service.ChatStatusService;
import com.example.mathengerapi.integrations.dspace.utils.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Async
@Component
@RequiredArgsConstructor
public class DSpaceMessageHandler {
    private final ChatStatusService chatStatusService;
    private final BotCommandsHandler botCommandsHandler;
    private final BotInfoHolder botInfoHolder;


    private static final String UUID_REGEX = "(?i)[a-f\\d]{8}-([a-f\\d]{4}-){3}[a-f\\d]{12}";

    @EventListener
    public void onNewMessage(MessageSent event) {
        if (chatStatusService.isActive(event.getChatId()) &&
                !event.getAuthorId().equals(botInfoHolder.getBotAccount().getId())) {
            handleBotMessage(event);
        }
    }
    private void handleAllCommunities(MessageSent event){
        botCommandsHandler.handleAllCommunities(event.getChatId());
    }
    private void handleAllCollectionsOfCommunity(MessageSent event){
        var message = event.getText();
        UUID communityId = CommandUtils.extractId(message);
        botCommandsHandler.handleAllCollectionsOfCommunity(event.getChatId(), communityId);
    }
    private void handleAllItemsOfCollection(MessageSent event){
        var message = event.getText();
        UUID collectionId = CommandUtils.extractId(message);
        botCommandsHandler.handleAllItemsOfCollection(event.getChatId(), collectionId);
    }
    private void handleAllCommands(MessageSent event){
        botCommandsHandler.handleAllCommands(event.getChatId());
    }
    private void handleInvalidCommand(MessageSent event){
        var message = event.getText();
        if (chatStatusService.isPrivateChat(event.getChatId())) {
            botCommandsHandler.handleInvalidCommand(event.getChatId(), message);
        }
    }
    private void handleBotMessage(MessageSent inputEvent) {
        Map<String, Consumer<MessageSent>> commandToConsumerMap = Map.of(
                "/community", event -> handleAllCommunities(event),
                "/community_" + UUID_REGEX, event -> handleAllCollectionsOfCommunity(event),
                "/colpublications_" + UUID_REGEX, event -> handleAllItemsOfCollection(event),
                "/help", event ->  handleAllCommands(event)
                );
        var message = inputEvent.getText();
        for(Map.Entry<String, Consumer<MessageSent>> entry : commandToConsumerMap.entrySet()) {
            if (message.matches(entry.getKey())) {
                entry.getValue().accept(inputEvent);
                return;
            }
        }
        handleInvalidCommand(inputEvent);
    }
}
