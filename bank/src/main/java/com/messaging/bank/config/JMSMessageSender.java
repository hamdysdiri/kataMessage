package com.messaging.bank.config;

import com.messaging.bank.service.MessageStorageService;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;


/**
 * Use @JmsListener for scalable, automatic, background processing.
 */
@Component
public class JMSMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(JMSMessageSender.class);

    @Value("${ibm.mq.user}")
    private String username;
    @Value("${ibm.mq.password}")
    private String password;
    @Value("${ibm.mq.queue}")
    private String mqQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    private final MessageStorageService messageStorageService;

    public JMSMessageSender(MessageStorageService messageStorageService) {
        this.messageStorageService = messageStorageService;
    }

    public void send(String messageText) {
        try {
            jmsTemplate.send(mqQueue, session -> {
                TextMessage message = (TextMessage) session.createTextMessage(messageText);
                logger.info("Preparing to send message: {}", messageText);
                saveMessageOnDB(message);
                return (jakarta.jms.Message) message;
            });

            logger.info("Message successfully sent to queue {}: {}", mqQueue, messageText);
        } catch (Exception e) {
            logger.error("Failed to send message to IBM MQ: {}", e.getMessage(), e);
        }
    }


    /**
     *
     * @param message the sended message, the parameter on the constructor is set to false
     * @throws JMSException
     */
    private void saveMessageOnDB(TextMessage message) throws jakarta.jms.JMSException {
        TextMessage textMessage = message;
        String content = textMessage.getText();
        messageStorageService.saveMessage(content,textMessage.getJMSMessageID(), false);
    }
}