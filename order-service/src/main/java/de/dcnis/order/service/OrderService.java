package de.dcnis.order.service;

import de.dcnis.shared.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public void sendUpdateToRocketMQ(Order order){


        // Update DB (new order)
        databaseService.createNewOrder();

        // MQ-Producer (send msg "I got new order")
        rocketMQTemplate.convertAndSend("order-add-topic", order);


    }

}
