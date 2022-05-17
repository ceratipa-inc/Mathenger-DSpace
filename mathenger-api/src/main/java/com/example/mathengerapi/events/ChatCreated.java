package com.example.mathengerapi.events;

import com.example.mathengerapi.models.enums.ChatType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatCreated {
    private Long chatId;
    private List<Long> memberIds;
    private ChatType chatType;
    private String name;
    private List<Long> adminIds;
}
