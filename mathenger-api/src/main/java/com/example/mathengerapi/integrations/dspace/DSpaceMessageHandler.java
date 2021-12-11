package com.example.mathengerapi.integrations.dspace;

import com.example.mathengerapi.events.MessageSent;
import com.example.mathengerapi.integrations.dspace.service.BotCommandsHandler;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.integrations.dspace.service.ChatStatusService;
import com.example.mathengerapi.integrations.dspace.utils.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
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
    private void handlePublication(MessageSent event){
        var message = event.getText();
        UUID publicationId = CommandUtils.extractId(message);
        botCommandsHandler.handlePublication(event.getChatId(), publicationId);
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
        List<Pair<String, Consumer<MessageSent>>> commandToConsumerList = List.of(
                Pair.of("/community", event -> handleAllCommunities(event)),
                Pair.of("/community_" + UUID_REGEX, event -> handleAllCollectionsOfCommunity(event)),
                Pair.of("/colpublications_" + UUID_REGEX, event -> handleAllItemsOfCollection(event)),
                Pair.of("/publication_" + UUID_REGEX, event -> handlePublication(event)),
                Pair.of("/help", event ->  handleAllCommands(event))
                );
        var message = inputEvent.getText();
        for(var entry : commandToConsumerList) {
            if (message.matches(entry.getFirst())) {
                entry.getSecond().accept(inputEvent);
                return;
            }
        }
        handleInvalidCommand(inputEvent);
    }
}
