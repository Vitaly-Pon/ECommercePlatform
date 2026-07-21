package com.vitaliy.inventoryservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import com.vitaliy.inventoryservice.event.InventoryEvent;

@Component
@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public void sendReserved(InventoryEvent event) {
        kafkaTemplate.send("inventory.reserved", event);
    }
}
