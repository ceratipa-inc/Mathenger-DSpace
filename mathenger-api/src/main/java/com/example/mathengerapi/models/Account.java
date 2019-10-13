package com.example.mathengerapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    @JsonIgnore
    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private User user;
    @Column(nullable = false, length = 15)
    private String firstName;
    @Column(length = 20)
    private String lastName;
    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date registrationDate;
    @Column(nullable = false, length = 15)
    private String color;
    @ManyToMany
    @JoinTable(name = "account_contact",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "contact_id", referencedColumnName = "id")})
    @JsonIgnore
    private List<Account> contacts;
    @ManyToMany
    @JoinTable(name = "account_chat",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id")})
    @JsonIgnore
    private List<Chat> chats;
    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Notification> notifications;
}
