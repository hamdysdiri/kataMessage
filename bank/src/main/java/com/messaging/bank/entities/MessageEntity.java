package com.messaging.bank.entities;

import com.messaging.bank.entities.enums.Direction;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String messageId;

    private String content;
    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private Direction direction;

    public MessageEntity( String content, String messageId, LocalDateTime receivedAt, Direction direction) {
        this.content = content;
        this.receivedAt = receivedAt;
        this.direction = direction;
    }

    public MessageEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public Direction getDirection() {
        return direction;
    }
}
