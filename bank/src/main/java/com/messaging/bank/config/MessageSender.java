package com.messaging.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.jms.*;


public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Value("${ibm.mq.username}")
    private String username;
    @Value("${ibm.mq.password}")
    private String password;
    private final QueueConnectionFactory factory;

    public MessageSender(QueueConnectionFactory factory) {
        this.factory = factory;
    }

    public void sendMessage(String messageText) {

        QueueConnection connection = null;
        QueueSession session = null;
        QueueSender sender = null;

        try {
            connection = factory.createQueueConnection(username,password);
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            // Suppose your queue is "QUEUE1"
            Queue queue = session.createQueue("QUEUE1");
            sender = session.createSender(queue);
            connection.start();

            TextMessage message = session.createTextMessage();
            message.setText(messageText);
            sender.send(message);

            logger.info("Message sent: " + messageText);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sender != null) sender.close();
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}