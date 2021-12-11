package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.models.message.Message;
import com.example.mathengerapi.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BotMessageService {
    private final BotInfoHolder dSpaceBotHolder;
    private final MessageService messageService;

    public static final String COMMANDS = Stream.of("/community - display a list of all communities",
                    "/community_{id} - display a list of collections that are in the selected community",
                    "/colpublications_{id} - display the list of all works in the collection",
                    "/publication_{id} - display information about publication",
                    "/help - display a list of all commands")
            .map(command -> command + "\n")
            .collect(Collectors.joining());


    public void sendWelcomeMessage(Long chatId) {
        String message = "Hello, I am DSpace chatbot. I'll help you to interact with DSpace. " +
                "You can talk to me with these commands:" + "\n" + COMMANDS;
        send(message, chatId);
    }


    @SneakyThrows
    public void send(String message, Long chatId) {
        Long botId = dSpaceBotHolder.getBotAccount().getId();
        Message messageEntity = new Message();
        messageEntity.setText(message);
        messageService.sendMessage(botId, messageEntity, chatId);
    }
}
