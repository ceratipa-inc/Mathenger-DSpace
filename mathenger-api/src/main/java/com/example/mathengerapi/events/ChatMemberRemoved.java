package com.example.mathengerapi.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMemberRemoved {
    private Long chatId;
    private Long memberId;
}
