package com.messaging.bank.entities;

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

    private boolean received;

    public MessageEntity( String content, String messageId, LocalDateTime receivedAt, boolean received) {
        this.content = content;
        this.receivedAt = receivedAt;
        this.received = received;
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

    public boolean isReceived() {
        return received;
    }
}
