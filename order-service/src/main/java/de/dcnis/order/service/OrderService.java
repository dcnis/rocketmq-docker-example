package de.dcnis.order.service;

import de.dcnis.order.mq.producer.AsyncProducer;
import de.dcnis.order.mq.producer.SyncProducer;
import de.dcnis.shared.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private SyncProducer syncProducer;

    @Autowired
    private AsyncProducer asyncProducer;

    public void sendUpdateToRocketMQ(Order order){


        // Update DB (new order)
        databaseService.createNewOrder();

        Instant start = Instant.now();
        log.info("start sending orders synchronously");
        syncProducer.sendUpdateSynchronously(order);
        Instant finish = Instant.now();
        log.info("done sending orders synchronously in {} ms", Duration.between(start, finish).toMillis());

//        Instant asyncStart = Instant.now();
//        log.info("start sending orders asynchronously");
//        asyncProducer.sendOrderAsynchronously(order);
//        Instant asyncFinish = Instant.now();
//        log.info("done sending orders asynchronously in {} ms", Duration.between(asyncStart, asyncFinish).toMillis());

    }



}
