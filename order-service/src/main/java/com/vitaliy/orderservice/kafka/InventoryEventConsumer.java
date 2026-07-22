package com.vitaliy.orderservice.kafka;

import com.vitaliy.inventoryservice.event.InventoryEvent;
import com.vitaliy.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "inventory.reserved", groupId = "order-group")
    public void handleInventoryReserved(InventoryEvent event) {
        log.info("Received inventory.reserved: reservationId={}, orderId={}, productId={}",
                event.getReservationId(), event.getOrderId(), event.getProductId());

        // Пока только логирование, статус меняется синхронно через gRPC
    }
}
