package com.messaging.bank.repository;

import com.messaging.bank.entities.MessageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MessageRepositoryIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void shouldStoreMessageInDatabaseAfterSend() {
        MessageEntity content = new MessageEntity("hello", "messageId", LocalDateTime.now(), false);
        messageRepository.save(content);
        List<MessageEntity> messages = messageRepository.findAll();
        assertThat(messages).isNotEmpty();
        MessageEntity saved = messages.get(0);
        assertThat(saved.getContent()).isEqualTo(content.getContent());
        assertThat(saved.getId()).isNotNull(); // MQ usually generates it
    }
}
