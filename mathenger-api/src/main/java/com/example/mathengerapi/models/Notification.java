package com.example.mathengerapi.models;

import com.example.mathengerapi.models.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    private NotificationType type;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Account receiver;
    @ManyToOne
    @JoinColumn(name = "producer_id", referencedColumnName = "id")
    private Account producer;
    @Lob
    private String text;
    @CreationTimestamp
    private LocalDateTime time;
}
