package com.messaging.bank.service;

import com.messaging.bank.config.JMS_V2.JMSMessageProcessor;
import com.messaging.bank.config.JMS_V1.MessageConsumer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MessageMQService {
    private final JMSMessageProcessor messageSender;
    private final MessageConsumer messageReceiver;


    public MessageMQService(JMSMessageProcessor messageSender, MessageConsumer messageReceiver, MessageStorageService messageStorageService) {
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
    }

    public void sendAndSaveMessage(final String messageText) {
        messageSender.asyncSend(messageText);
    }

    public String receiveAndSaveMessage()  {
        return messageReceiver.receiveMessageManuallyFromTheQueue();
    }
}
