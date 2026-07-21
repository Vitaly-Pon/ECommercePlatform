package com.vitaliy.notificationservice.kafka;

import com.vitaliy.notificationservice.service.NotificationService;
import com.vitaliy.orderservice.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.vitaliy.inventoryservice.event.InventoryEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventConsumer {

    private final NotificationService service;

    @KafkaListener(topics = "order.created", groupId = "notification-group")
    public void handleOrderCreated(OrderEvent event) {
        // event.getUserId(), event.getOrderId() — как у Avro-класса
        service.create(
                event.getUserId().toString(),
                "user@example.com",
                "ORDER_CREATED",
                event.getOrderId(),
                "Order Created",
                "Your order #" + event.getOrderId() + " has been created"
        );
    }

    @KafkaListener(topics = "order.status-changed", groupId = "notification-group")
    public void handleOrderStatusChanged(OrderEvent event) {
        log.info("Received order.status-changed: {}", event);
        service.create(
                event.getUserId().toString(),
                "user@example.com",
                "ORDER_STATUS_CHANGED",
                event.getOrderId(),
                "Order Status Updated",
                "Your order #" + event.getOrderId() + " is now " + event.getStatus()
        );
    }
    @KafkaListener(topics = "inventory.reserved", groupId = "notification-group")
    public void handleInventoryReserved(InventoryEvent event) {

        log.info(
                "Inventory reserved: productId={}, quantity={}, reservationId={}",
                event.getProductId(),
                event.getQuantity(),
                event.getReservationId()
        );

        service.create(
                "SYSTEM",
                "system@example.com",
                "INVENTORY_RESERVED",
                null,
                "Inventory Reserved",
                "Product " + event.getProductId()
                        + " reserved, quantity="
                        + event.getQuantity()
        );
    }
}
