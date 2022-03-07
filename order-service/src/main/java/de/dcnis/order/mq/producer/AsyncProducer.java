package de.dcnis.order.mq.producer;

import de.dcnis.order.enums.Topic;
import de.dcnis.shared.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendOrderAsynchronously(Order order) {

        for(int i = 0; i < 1000; i++){
            order.setId(i);
            order.setProduct("asynchronous Product");
            log.info("Producer sends asynchronous newOrderMsg {}/{}", i, 999);
            rocketMQTemplate.asyncSend(Topic.ORDER_ADD_TOPIC.getValue(), order, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("Asynchronous order with msgId {} send successfully", sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("Problem occurred sending asynchronous order {}!", throwable.getMessage());
                }
            });
        }

        log.info("All orders asynchronously delivered");

    }
}
