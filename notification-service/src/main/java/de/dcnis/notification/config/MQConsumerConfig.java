package de.dcnis.notification.config;

import de.dcnis.notification.listener.NotificationNewOrderListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class MQConsumerConfig {
    private DefaultMQPushConsumer consumer;
    
    @Value("${rocketmq.consumer.group-name}")
    private String groupName;
    @Value("${rocketmq.consumer.nameserver-address}")
    private String nameserverAddr;
    @Value("${rocketmq.consumer.topic}")
    private String topic;
    @Value("${rocketmq.consumer.consume-failure-retry-times}")
    private Integer retryTimes;

    @Autowired
    private NotificationNewOrderListener notificationNewOrderListener;

    @PostConstruct
    public void init() throws MQClientException {
        this.consumer = new DefaultMQPushConsumer(groupName);
        this.consumer.setNamesrvAddr(nameserverAddr);
        this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        this.consumer.subscribe(topic, "*");
        this.consumer.registerMessageListener(notificationNewOrderListener);
        this.consumer.start();
        log.info("consumer started!");
    }
}