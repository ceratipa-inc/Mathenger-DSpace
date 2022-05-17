package com.example.mathengerapi.events;

import com.example.mathengerapi.models.message.MathFormula;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageSent {
    private Long messageId;
    private Long authorId;
    private Long senderId;
    private LocalDateTime time;
    private String text;
    private MathFormula mathFormula;
    private Long chatId;
}
