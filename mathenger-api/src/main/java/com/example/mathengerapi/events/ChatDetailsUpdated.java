package com.example.mathengerapi.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDetailsUpdated {
    private Long chatId;
    private String name;
}
