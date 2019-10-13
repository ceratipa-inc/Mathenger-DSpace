package com.example.mathengerapi.controllers;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.services.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/chats")
public class ChatController {
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> getMyChats(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(chatService.findByUserId(user.getId()), HttpStatus.OK);
    }

    @PostMapping("/new/{contact}")
    public ResponseEntity<Chat> startPrivateChat(@AuthenticationPrincipal User user, @PathVariable Account contact) {
        return new ResponseEntity<>(chatService.startPrivateChat(user.getId(), contact), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{chat}")
    public ResponseEntity removeChat(@AuthenticationPrincipal User user, @PathVariable Chat chat) {
        chatService.delete(user.getId(), chat);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
