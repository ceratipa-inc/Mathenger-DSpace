package com.example.mathengerapi.models;

import com.example.mathengerapi.models.enums.AccountType;
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

import static com.example.mathengerapi.models.enums.AccountType.REGULAR;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", length = 30)
    private AccountType accountType = REGULAR;

    @JsonIgnore
    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(nullable = false, length = 15)
    private String firstName;

    @Column(length = 20)
    private String lastName;

    @Temporal(value = TemporalType.DATE)
    @Column
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
