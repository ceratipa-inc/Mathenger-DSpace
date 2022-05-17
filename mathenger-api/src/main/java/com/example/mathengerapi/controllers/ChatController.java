package com.example.mathengerapi.controllers;

import com.example.mathengerapi.dto.ChatsDTO;
import com.example.mathengerapi.models.*;
import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.services.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/chats")
public class ChatController {
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<ChatsDTO> getMyChats(@AuthenticationPrincipal User user) {
        var chats = chatService.findByUserId(user.getId());
        var privateChats = chats.stream()
                .filter(chat -> chat.getChatType().equals(ChatType.PRIVATE_CHAT))
                .map(chat -> (PrivateChat) chat)
                .collect(Collectors.toList());
        var groupChats = chats.stream()
                .filter(chat -> chat.getChatType().equals(ChatType.GROUP_CHAT))
                .map(chat -> (GroupChat) chat)
                .collect(Collectors.toList());
        var chatsDTO = new ChatsDTO(privateChats, groupChats);
        return new ResponseEntity<>(chatsDTO, HttpStatus.OK);
    }

    @PostMapping("/contacts/{contact}")
    public ResponseEntity<Chat> startPrivateChat(@AuthenticationPrincipal User user, @PathVariable Account contact) {
        return new ResponseEntity<>(chatService.startPrivateChat(user.getId(), contact), HttpStatus.CREATED);
    }

    @PutMapping("/{chat}")
    public ResponseEntity<GroupChat> updateChat(@AuthenticationPrincipal User user, @PathVariable GroupChat chat,
                                                @RequestBody GroupChat newChat) throws JsonProcessingException {
        return new ResponseEntity<>(chatService.updateGroupChat(user.getId(), chat, newChat), HttpStatus.OK);
    }

    @PutMapping("/{chat}/leave")
    public ResponseEntity leaveChat(@AuthenticationPrincipal User user,
                                    @PathVariable GroupChat chat) throws JsonProcessingException {
        chatService.leave(user.getId(), chat);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{chat}/admins/{member}")
    public ResponseEntity<GroupChat> addAdmin(@AuthenticationPrincipal User user, @PathVariable GroupChat chat,
                                              @PathVariable Account member) throws JsonProcessingException {
        return new ResponseEntity<>(chatService.addAdmin(user.getId(), chat, member), HttpStatus.OK);
    }

    @DeleteMapping("/{chat}/admins/{member}")
    public ResponseEntity<GroupChat> removeAdmin(@AuthenticationPrincipal User user, @PathVariable GroupChat chat,
                                                 @PathVariable Account member) throws JsonProcessingException {
        return new ResponseEntity<>(chatService.removeAdmin(user.getId(), chat, member), HttpStatus.OK);
    }

    @PostMapping("/{chat}/members")
    public ResponseEntity<GroupChat> addMembers(@AuthenticationPrincipal User user, @PathVariable GroupChat chat,
                                                @RequestBody List<Account> newMembers) throws JsonProcessingException {
        return new ResponseEntity<>(chatService.addMembers(user.getId(), chat, newMembers), HttpStatus.OK);
    }

    @DeleteMapping("/{chat}/members/{member}")
    public ResponseEntity<GroupChat> removeMember(@AuthenticationPrincipal User user, @PathVariable GroupChat chat,
                                                  @PathVariable Account member) throws JsonProcessingException {
        return new ResponseEntity<>(chatService.removeMember(user.getId(), chat, member), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GroupChat> startGroupChat(@AuthenticationPrincipal User user,
                                                    @RequestBody GroupChat groupChat) throws JsonProcessingException {
        return new ResponseEntity<>(chatService.startGroupChat(user.getId(), groupChat), HttpStatus.CREATED);
    }

    @DeleteMapping("/{chat}")
    public ResponseEntity removeChat(@AuthenticationPrincipal User user, @PathVariable Chat chat) {
        chatService.delete(user.getId(), chat);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
