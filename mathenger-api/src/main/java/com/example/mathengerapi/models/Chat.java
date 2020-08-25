package com.example.mathengerapi.models;

import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.models.message.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Data
@DiscriminatorColumn(name = "chat_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Chat {
    @JsonIgnore
    @Transient
    public static final int PAGE_SIZE = 20;

    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany
    @JoinTable(name = "chat_member",
            joinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")})
    private List<Account> members;
    @JsonIgnore
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @Transient
    @JsonProperty("messages")
    public List<Message> getFirstMessagesPage() {
        var sorted = messages.stream().sorted((m1, m2) -> -m1.getTime().compareTo(m2.getTime()))
                .collect(Collectors.toList());
        return (sorted.size() > PAGE_SIZE) ? sorted.subList(0, PAGE_SIZE) : sorted;
    }

    public abstract ChatType getChatType();

    public abstract void update(Chat chat);
}
