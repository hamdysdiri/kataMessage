package com.messaging.bank.service;

import com.messaging.bank.config.JMS.JMSMessageSender;
import com.messaging.bank.config.MessageConsumer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MessageMQService {
    private final JMSMessageSender messageSender;
    private final MessageConsumer messageReceiver;

    public MessageMQService(JMSMessageSender messageSender, MessageConsumer messageReceiver) {
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
    }

    public void sendAndSaveMessage(final String messageText) {
        messageSender.asyncSend(messageText);
    }

    public String receiveAndSaveMessage()  {
        return messageReceiver.receiveMessage();
    }

}
