package com.vitaliy.orderservice.kafka;

import com.vitaliy.orderservice.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderCreated(OrderEvent event) {
        kafkaTemplate.send("order.created", event);
    }

    public void sendOrderStatusChanged(OrderEvent event) {
        kafkaTemplate.send("order.status-changed", event);
    }
}
