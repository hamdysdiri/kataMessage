package com.messaging.bank.config;

import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.compat.jms.internal.JMSC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Value("${ibm.mq.connName}")
    private String host;
    @Value("${ibm.mq.port}")
    private Integer port;
    @Value("${ibm.mq.channel}")
    private String channel;
    @Value("${ibm.mq.queueManager}")
    private String queue;


    @Bean
    public QueueConnectionFactory createConnectionFactory() throws JMSException {
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setQueueManager(queue);
        factory.setChannel(channel);
        factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
        return factory;
    }

}
