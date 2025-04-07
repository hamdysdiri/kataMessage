package com.messaging.bank.config.JMS_V2;

import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.service.MessageStorageService;
import jakarta.annotation.PreDestroy;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.concurrent.ExecutorService;


/**
 * V2:
 * Use @JmsListener for scalable, automatic, background processing.
 */
@Component
public class JMSMessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(JMSMessageProcessor.class);
    @Value("${ibm.mq.queue}")
    private String mqQueue;

    private final JmsTemplate jmsTemplate;

    private final MessageStorageService messageStorageService;

    private final ExecutorService executorService;

    public JMSMessageProcessor(JmsTemplate jmsTemplate, MessageStorageService messageStorageService, ExecutorService executorService) {
        this.jmsTemplate = jmsTemplate;
        this.messageStorageService = messageStorageService;
        this.executorService = executorService;

    }

    public void asyncSend(String messageText) {
        executorService.submit(() -> {
            try {
                jmsTemplate.send(mqQueue, session -> {
                    TextMessage message = session.createTextMessage(messageText);
                    logger.info("Preparing to send message: {}", messageText);
                    try {
                        saveMessageOnDB(message);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return message;
                });

                logger.info("Message successfully sent to queue {}: {}", mqQueue, messageText);
            } catch (Exception e) {
                logger.error("Failed to send message to IBM MQ: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * the JMS Listner will lissen to the queue for new message, at this moment I am pulling the message and stored in the db
     * the retryable https://www.baeldung.com/spring-retry is used to cover up the failed operations like network failure.
     * @param message
     */
    @Retryable(
            maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}")
    )
    @JmsListener(destination = "${ibm.mq.queue}", containerFactory = "jmsListenerContainerFactory")
    public void receiveFromQueue(TextMessage message) {
        try {
            String messageText = message.getText();
            logger.info("Received from queue '{}': {}", mqQueue, messageText);
            messageStorageService.saveMessage(messageText, message.getJMSMessageID(), Direction.OUTBOUND);
        } catch (Exception e) {
            logger.error("Failed to process message from queue: {}", e.getMessage(), e);
        }
    }



    /**
     *
     * @param message the sended message, the parameter on the constructor is set to false
     * @throws JMSException
     */
    private void saveMessageOnDB(TextMessage message) throws Exception {
        TextMessage textMessage = message;
        String content = textMessage.getText();
        messageStorageService.saveMessage(content,textMessage.getJMSMessageID(), Direction.INBOUND);
    }

    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdown();
    }
}