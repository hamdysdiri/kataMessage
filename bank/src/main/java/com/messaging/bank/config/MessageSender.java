package com.messaging.bank.config;

import com.messaging.bank.service.MessageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jms.*;


@Component
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Value("${ibm.mq.user}")
    private String username;
    @Value("${ibm.mq.password}")
    private String password;
    @Value("${ibm.mq.queue}")
    private String mqQueue;

    private final QueueConnectionFactory factory;
    private final MessageStorageService messageStorageService;

    public MessageSender(QueueConnectionFactory factory, MessageStorageService messageStorageService) {
        this.factory = factory;
        this.messageStorageService = messageStorageService;
    }

    public void sendMessage(String messageText) {

        QueueConnection connection = null;
        QueueSession session = null;
        QueueSender sender = null;

        try {
            connection = factory.createQueueConnection(username,password);
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(mqQueue);
            sender = session.createSender(queue);
            connection.start();

            TextMessage message = session.createTextMessage();
            message.setText(messageText);
            sender.send(message);

            saveMessageOnDB(message);

            logger.info("Message sent: {}" , messageText);
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

    private void saveMessageOnDB(TextMessage message) throws JMSException {
        TextMessage textMessage = message;
        String content = textMessage.getText();
        messageStorageService.saveMessage(content,textMessage.getJMSMessageID());
    }
}