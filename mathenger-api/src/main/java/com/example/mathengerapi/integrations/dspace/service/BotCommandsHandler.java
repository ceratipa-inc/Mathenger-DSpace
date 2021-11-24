package com.example.mathengerapi.integrations.dspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotCommandsHandler {
    private final DSpaceClient dSpaceClient;
    private final BotMessageService botMessageService;

    public void handleAllCommunities(Long chatId) {
        var communities = dSpaceClient.getCommunities();
        String message = communities.stream()
                .map(community -> community.toString() + "\n")
                .collect(Collectors.joining());
        botMessageService.send(message, chatId);
    }
}
