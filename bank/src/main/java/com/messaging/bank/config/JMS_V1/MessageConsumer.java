package com.messaging.bank.config.JMS_V1;

import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.service.MessageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Queue;
import javax.jms.TextMessage;

@Component
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final QueueConnectionFactory factory;
    private final MessageStorageService messageStorageService;

    @Value("${ibm.mq.user}")
    private String username;
    @Value("${ibm.mq.password}")
    private String password;

    @Value("${ibm.mq.queue}")
    private String mqQueue;

    public MessageConsumer(QueueConnectionFactory factory, MessageStorageService messageStorageService) {
        this.factory = factory;
        this.messageStorageService = messageStorageService;

    }

    public String receiveMessageManuallyFromTheQueue() {

        QueueConnection connection = null;
        QueueSession session = null;
        QueueReceiver receiver = null;

        try {
            System.out.println("receiver = " + receiver);
            connection = factory.createQueueConnection(username, password);
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(mqQueue);
            receiver = session.createReceiver(queue);
            connection.start();

            Message message = receiver.receive(1000);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                long timestamp = message.getJMSTimestamp();
                String messageId = message.getJMSMessageID();
                logger.info("Message received: {} ", textMessage.getText());
                logger.info("Message ID: {} ", messageId);
                logger.info("Message timestamp: {} ", timestamp);

                messageStorageService.saveMessage(textMessage.getText(), textMessage.getJMSMessageID(), Direction.OUTBOUND);

                return textMessage.getText();
            } else {
                logger.info("No text message received.");
                return null;
            }
        } catch (JMSException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (receiver != null) receiver.close();
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}