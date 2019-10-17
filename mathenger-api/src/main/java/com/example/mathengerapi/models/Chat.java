package com.example.mathengerapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chat {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private final ChatType chatType = ChatType.PRIVATE_CHAT;
    @ManyToMany
    @JoinTable(name = "chat_member",
            joinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")})
    private Set<Account> members;
    @OneToMany
    @JoinTable(name = "chat_message",
            joinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "message_id", referencedColumnName = "id")})
    private List<Message> messages;
}
