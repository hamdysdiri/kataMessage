package com.messaging.bank.service;

import com.messaging.bank.entities.MessageEntity;
import com.messaging.bank.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageStorageService {

    private final MessageRepository repository;

    public MessageStorageService(MessageRepository repository) {
        this.repository = repository;
    }

    public void saveMessage( String content, String messageId ) {
        MessageEntity entity = new MessageEntity(content, messageId,
                LocalDateTime.now());
        repository.save(entity);
    }
}
