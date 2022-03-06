package de.dcnis.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dcnis.shared.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Dennis Schmidt (dcnis)
 */
@Service
@Slf4j
@RocketMQMessageListener(topic = "order-add-topic", consumerGroup = "notification-consumer_order-add-topic")
public class NotificationNewOrderListener implements RocketMQListener<Order> {

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public void onMessage(Order order) {
        log.info("Receiving new order message {}", order);

        // send email to user




    }


}