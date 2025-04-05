package com.messaging.bank.service;

import com.messaging.bank.config.MessageReceiver;
import com.messaging.bank.config.MessageSender;
import org.springframework.stereotype.Service;


@Service
public class MessageService {
    private final MessageSender messageSender;
    private final MessageReceiver messageReceiver;

    public MessageService(MessageSender messageSender, MessageReceiver messageReceiver) {
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
    }

    public void sendAndSaveMessage(String messageText) {
        messageSender.sendMessage(messageText);
    }

    public String receiveAndSaveMessage() {
        return messageReceiver.receiveMessage();
    }

}
