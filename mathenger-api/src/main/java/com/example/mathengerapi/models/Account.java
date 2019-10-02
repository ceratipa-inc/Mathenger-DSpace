package com.example.mathengerapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User user;
    @Column(nullable = false, length = 15)
    private String firstName;
    @Column(length = 20)
    private String lastName;
    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    @CreationTimestamp
    private Date registrationDate;
    @Column(nullable = false, length = 15)
    private String color;
    @ManyToMany
    @JoinTable(name = "account_contact",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "contact_id", referencedColumnName = "id")})
    private List<Account> contacts;
    @ManyToMany
    @JoinTable(name = "account_chat",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id")})
    private List<Chat> chats;
    @OneToMany(mappedBy = "receiver")
    private List<Notification> notifications;
}
