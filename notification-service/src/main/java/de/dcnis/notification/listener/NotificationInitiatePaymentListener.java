package de.dcnis.notification.listener;

import de.dcnis.shared.domain.Payment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;


/**
 * @author Dennis Schmidt (dcnis)
 */
@Service
@Slf4j
@RocketMQMessageListener(topic = "initiate-payment-topic", consumerGroup = "notification-consumer_initiate-payment-topic")
public class NotificationInitiatePaymentListener implements RocketMQListener<Payment> {

    @SneakyThrows
    @Override
    public void onMessage(Payment payment) {
        log.info("Receiving new payment message {}", payment);

        Thread.sleep(5000);
        log.info("done");
        // send email to user
    }


}
