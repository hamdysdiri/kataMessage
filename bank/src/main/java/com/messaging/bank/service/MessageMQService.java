package com.messaging.bank.service;

import com.messaging.bank.config.MessageReceiver;
import com.messaging.bank.config.MessageSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MessageMQService {
    private final MessageSender messageSender;
    private final MessageReceiver messageReceiver;

    public MessageMQService(MessageSender messageSender, MessageReceiver messageReceiver) {
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
    }

    public void sendAndSaveMessage(final String messageText) {
        messageSender.sendMessage(messageText);
    }

    public String receiveAndSaveMessage() {
        return messageReceiver.receiveMessage();
    }

}
