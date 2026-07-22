package com.vitaliy.orderservice.kafka;

import com.vitaliy.inventoryservice.event.InventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    @KafkaListener(topics = "inventory.reserved", groupId = "order-group")
    public void handleInventoryReserved(InventoryEvent event) {
        log.info(
                "Received inventory.reserved: reservationId={}, productId={}, status={}",
                event.getReservationId(),
                event.getProductId(),
                event.getStatus()
        );
        // TODO: После добавления orderId в InventoryEvent
        // обновлять статус заказа на RESERVED
    }
}
