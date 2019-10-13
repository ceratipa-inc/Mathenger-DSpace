package com.example.mathengerapi.models;

import javax.persistence.Column;

public class GroupChat extends Chat {
    @Column(length = 40)
    private String name;
    private final ChatType chatType = ChatType.GROUP_CHAT;
}
