package com.example.mathengerapi.models;

import com.example.mathengerapi.models.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue(value = "GROUP_CHAT")
public class GroupChat extends Chat {
    @Column(length = 40)
    private String name;
    @Column(nullable = false, length = 15)
    private String color;
    @Transient
    private final ChatType chatType = ChatType.GROUP_CHAT;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Account creator;
    @ManyToMany
    @JoinTable(name = "chat_admin",
            joinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<Account> admins;
}
