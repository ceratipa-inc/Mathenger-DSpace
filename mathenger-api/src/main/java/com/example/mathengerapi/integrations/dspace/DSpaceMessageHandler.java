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


    @EventListener
    public void onNewMessage(MessageSent event) {
        if (chatStatusService.isActive(event.getChatId())) {
            handleBotMessage(event);
        }
    }

    private void handleBotMessage(MessageSent event) {
        // TODO refactor to support many different commands without bunch of "if" blocks
        var message = event.getText();
        if ("/community".equals(message)) {
            botCommandsHandler.handleAllCommunities(event.getChatId());
        }
        else if(message.startsWith("/community_{")){
            UUID collectionId =UUID.fromString(message.substring(12,message.length()-1));
            botCommandsHandler.handleAllCollectionsOfCommunity(event.getChatId(), collectionId);
        }

    }
}
