package com.example.mathengerapi.controllers;

import com.example.mathengerapi.models.Message;
import com.example.mathengerapi.services.AuthenticationService;
import com.example.mathengerapi.services.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
@AllArgsConstructor
public class MessageController {
    private MessageService messageService;
    private AuthenticationService authenticationService;

    @MessageMapping("chat/{chatId}/send")
    public void sendMessage(@Header(name = "Authorization") String token,
                            @Payload Message message, @DestinationVariable Long chatId) throws JsonProcessingException {
        messageService.sendMessage(authenticationService.getUserId(token.replace("Bearer ", "")), message, chatId);
    }

}
