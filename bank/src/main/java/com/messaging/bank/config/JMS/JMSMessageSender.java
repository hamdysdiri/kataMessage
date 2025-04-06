package com.messaging.bank.config.JMS;

import com.messaging.bank.service.MessageStorageService;
import jakarta.annotation.PreDestroy;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.concurrent.ExecutorService;


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

    private final JmsTemplate jmsTemplate;

    private final MessageStorageService messageStorageService;

    private final ExecutorService executorService;

    public JMSMessageSender(JmsTemplate jmsTemplate, MessageStorageService messageStorageService, ExecutorService executorService) {
        this.jmsTemplate = jmsTemplate;
        this.messageStorageService = messageStorageService;
        this.executorService = executorService;

    }

   //@JmsListener(destination = "${ibm.mq.queue}", containerFactory = "jmsListenerContainerFactory")
    public void asyncSend(String messageText) {
        executorService.submit(() -> {
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
        });
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

    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdown();
    }
}