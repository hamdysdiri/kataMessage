package com.messaging.bank.service;

import com.messaging.bank.entities.MessageEntity;
import com.messaging.bank.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MessageStorageService {

    private final MessageRepository repository;

    public MessageStorageService(MessageRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @param content 's message
     * @param messageId the jms id
     * @param received received true, sended false
     */
    public void saveMessage(final String content, final String messageId , final boolean received) {
        MessageEntity entity = new MessageEntity(content, messageId,
                LocalDateTime.now(), received);
        repository.save(entity);
    }

    /**
     *
     * @return all the message saved on the DB
     */
    @Transactional(readOnly = true)
    public List<MessageEntity> getAllMessagesStorage(){
       return repository.findAll();
    }
}
