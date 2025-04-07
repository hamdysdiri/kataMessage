package com.messaging.bank.repository;

import com.messaging.bank.entities.MessageEntity;
import com.messaging.bank.entities.enums.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    MessageEntity findFirstByDirectionOrderByReceivedAtDesc(Direction direction);

}

