package com.messaging.bank.config;

import javax.jms.JMSException;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.compat.jms.internal.JMSC;
import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.EnableRetry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableRetry
@EnableJms
public class MqConfig {

    @Value("${ibm.mq.connName}")
    private String host;
    @Value("${ibm.mq.port}")
    private Integer port;
    @Value("${ibm.mq.channel}")
    private String channel;
    @Value("${ibm.mq.queueManager}")
    private String queue;
    @Value("${ibm.mq.user}")
    private String username;
    @Value("${ibm.mq.password}")
    private String password;

    @Bean
    public MQQueueConnectionFactory connectionFactory () throws JMSException {
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setQueueManager(queue);
        factory.setChannel(channel);
        factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);

        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setReceiveTimeout(2000);
        return template;
    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("5-10");
        return factory;
    }

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(10);
    }

}
