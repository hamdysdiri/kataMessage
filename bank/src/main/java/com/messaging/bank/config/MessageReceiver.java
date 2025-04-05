package com.messaging.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Queue;
import javax.jms.TextMessage;

public class MessageReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final QueueConnectionFactory factory;

    public MessageReceiver(QueueConnectionFactory factory) {
        this.factory = factory;
    }

    public String receiveMessage() {

        QueueConnection connection = null;
        QueueSession session = null;
        QueueReceiver receiver = null;

        try {
            connection = factory.createQueueConnection("app", "passw0rd");
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            // Suppose your queue is "QUEUE1"
            Queue queue = session.createQueue("QUEUE1");
            receiver = session.createReceiver(queue);
            connection.start();

            Message message = receiver.receive(1000); // 1 second timeout, adjust to your needs

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                long timestamp = message.getJMSTimestamp();
                String messageId = message.getJMSMessageID();
                int priority = message.getJMSPriority();
                long expiration = message.getJMSExpiration();

                logger.info("Message received: " + textMessage.getText());
                logger.info("Message ID: " + messageId);
                logger.info("Message timestamp: " + timestamp);
                logger.info("Message Priority: " + priority);
                logger.info("Expiration Time: " + expiration);

                return textMessage.getText();
            } else {
                logger.info("No text message received.");
                return null;
            }
        } catch (JMSException e) {
            // handle exception
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (receiver != null) receiver.close();
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                // handle exception
                e.printStackTrace();
            }
        }
    }
}