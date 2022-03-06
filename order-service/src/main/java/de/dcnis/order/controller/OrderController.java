package de.dcnis.order.controller;

import de.dcnis.order.service.OrderService;
import de.dcnis.shared.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/newOrder")
    public void sendUpdate(@RequestBody Order order){
        this.orderService.sendUpdateToRocketMQ(order);
    }
}
