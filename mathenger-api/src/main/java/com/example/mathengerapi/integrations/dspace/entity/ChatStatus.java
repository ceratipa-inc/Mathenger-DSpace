package com.example.mathengerapi.integrations.dspace.entity;

import com.example.mathengerapi.models.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dspace_chat_status")
public class ChatStatus {
    @Id
    private Long chatId;

    @Column(length = 40)
    private String chatName;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    private boolean isActive;
}

