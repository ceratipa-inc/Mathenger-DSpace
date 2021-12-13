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

    private void handleBotMessage(MessageSent inputEvent) {
        List<Pair<String, Consumer<MessageSent>>> commandToConsumerList = List.of(
                Pair.of("/community", this::handleAllCommunities),
                Pair.of("/community_" + UUID_REGEX, this::handleAllCollectionsOfCommunity),
                Pair.of("/colpublications_" + UUID_REGEX, this::handleAllItemsOfCollection),
                Pair.of("/publication_" + UUID_REGEX, this::handlePublication),
                Pair.of("/help", this::handleAllCommands)
        );
        var message = inputEvent.getText();
        commandToConsumerList.stream()
                .filter(entry -> message.matches(entry.getFirst()))
                .map(Pair::getSecond)
                .findFirst()
                .ifPresentOrElse(consumer -> consumer.accept(inputEvent), () -> handleInvalidCommand(inputEvent));
    }

    private void handleAllCommunities(MessageSent event) {
        botCommandsHandler.handleAllCommunities(event.getChatId());
    }

    private void handleAllCollectionsOfCommunity(MessageSent event) {
        var message = event.getText();
        UUID communityId = CommandUtils.extractId(message);
        botCommandsHandler.handleAllCollectionsOfCommunity(event.getChatId(), communityId);
    }

    private void handleAllItemsOfCollection(MessageSent event) {
        var message = event.getText();
        UUID collectionId = CommandUtils.extractId(message);
        botCommandsHandler.handleAllItemsOfCollection(event.getChatId(), collectionId);
    }

    private void handlePublication(MessageSent event) {
        var message = event.getText();
        UUID publicationId = CommandUtils.extractId(message);
        botCommandsHandler.handlePublication(event.getChatId(), publicationId);
    }

    private void handleAllCommands(MessageSent event) {
        botCommandsHandler.handleAllCommands(event.getChatId());
    }

    private void handleInvalidCommand(MessageSent event) {
        var message = event.getText();
        if (chatStatusService.isPrivateChat(event.getChatId())) {
            botCommandsHandler.handleInvalidCommand(event.getChatId(), message);
        }
    }
}
