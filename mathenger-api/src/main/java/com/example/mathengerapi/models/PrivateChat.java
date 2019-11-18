package com.example.mathengerapi.models;

import com.example.mathengerapi.models.enums.ChatType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue(value = "PRIVATE_CHAT")
public class PrivateChat extends Chat {
    @Transient
    private final ChatType chatType = ChatType.PRIVATE_CHAT;

    @Override
    public void update(Chat chat) {

    }
}
