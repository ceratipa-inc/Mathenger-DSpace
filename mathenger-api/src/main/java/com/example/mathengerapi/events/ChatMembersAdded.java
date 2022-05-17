package com.example.mathengerapi.events;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatMembersAdded {
    private Long chatId;
    private List<Long> newMemberIds;
}
