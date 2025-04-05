package com.messaging.bank.service;

import com.messaging.bank.config.MessageReceiver;
import com.messaging.bank.config.MessageSender;
import org.springframework.stereotype.Service;

import javax.jms.QueueConnectionFactory;


@Service
public class MessageService {
    private final QueueConnectionFactory queueConnectionFactory;

    public MessageService(QueueConnectionFactory queueConnectionFactory) {
        this.queueConnectionFactory = queueConnectionFactory;
    }

    public void sendMessage(String messageText) {
        // Create a sender with the injected factory
        MessageSender sender = new MessageSender(queueConnectionFactory);
        sender.sendMessage(messageText);
    }

    public String receiveMessage() {
        // Create a receiver with the injected factory
        MessageReceiver receiver = new MessageReceiver(queueConnectionFactory);
        return receiver.receiveMessage();
    }
}
