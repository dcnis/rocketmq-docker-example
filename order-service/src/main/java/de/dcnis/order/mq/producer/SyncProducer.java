package de.dcnis.order.mq.producer;


import de.dcnis.order.enums.Topic;
import de.dcnis.shared.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SyncProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * There will be 100 orders send in total. They are sent synchronously, which means the log message "All orders delivered"
     * will be outputted after all orders are sent to the message queue.
     * The consumer on the other hand will handle the orders in no specific order.
     * @param order templateOrder
     */
    public void sendUpdateSynchronously(Order order) {
        for(int i = 0; i < 1000; i++){
            order.setId(i);
            order.setProduct("synchronous Product");
            log.info("Producer sends synchronous newOrderMsg {}/{}", i, 999);
            rocketMQTemplate.convertAndSend(Topic.ORDER_ADD_TOPIC.getValue(), order);
            log.info("Synchronous order {} send successfully", order.getId());
        }

        log.info("All orders synchronously delivered");

    }

}
