package com.example.mathengerapi.integrations.dspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotCommandsHandler {
    private final DSpaceClient dSpaceClient;
    private final BotMessageService botMessageService;

    public void handleAllCommunities(Long chatId) {
        var communities = dSpaceClient.getCommunities();
        String message = "Here is a list of all communities:\n\n" + communities.stream()
                .map(community -> community.toString() + "\n")
                .collect(Collectors.joining()) + "\nTo view more about one of them, use the command “/community_{id}”";
        botMessageService.send(message, chatId);
    }

    public void handleAllCollectionsOfCommunity(Long chatId,UUID uuid ){
        var collections=dSpaceClient.getCollectionsOfCommunity(uuid);
        String message = collections.stream()
                .map(collection -> collection.toString() + "\n")
                .collect(Collectors.joining());
        botMessageService.send(message, chatId);
    }
}
