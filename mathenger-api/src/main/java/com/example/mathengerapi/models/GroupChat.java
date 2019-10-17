package com.example.mathengerapi.models;

import javax.persistence.Column;

public class GroupChat extends Chat {
    @Column(length = 40)
    private String name;
    @Column(nullable = false, length = 15)
    private String color;
    private final ChatType chatType = ChatType.GROUP_CHAT;
}
