package com.example.mathengerapi.models.message;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Account author;
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Account sender;
    @CreationTimestamp
    private LocalDateTime time;
    @Lob
    private String text;
    @Embedded
    private MathFormula mathFormula;
    @ManyToOne
    @JsonIgnore
    private Chat chat;
}
