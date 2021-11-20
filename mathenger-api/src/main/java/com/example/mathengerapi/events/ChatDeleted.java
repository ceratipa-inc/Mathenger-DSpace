package com.example.mathengerapi.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDeleted {
    private Long chatId;
}
