package com.vitaliy.orderservice.service;

import com.vitaliy.orderservice.domain.Order;
import com.vitaliy.orderservice.domain.OrderItem;
import com.vitaliy.orderservice.domain.OrderStatus;
import com.vitaliy.orderservice.dto.OrderItemRequest;
import com.vitaliy.orderservice.dto.OrderItemResponse;
import com.vitaliy.orderservice.dto.OrderRequest;
import com.vitaliy.orderservice.dto.OrderResponse;
import com.vitaliy.orderservice.event.OrderEvent;
import com.vitaliy.orderservice.kafka.OrderProducer;
import com.vitaliy.orderservice.repository.OrderRepository;
import com.vitaliy.orderservice.grpc.GrpcInventoryClient;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderProducer orderProducer;
    private final GrpcInventoryClient inventoryClient;

    @Transactional
    public OrderResponse create(OrderRequest request) {

        // Проверка наличия
        for (OrderItemRequest item : request.getItems()) {
            boolean available = inventoryClient.checkStock(item.getProductId(), item.getQuantity());
            if (!available) {
                throw new RuntimeException("Not enough stock for product: " + item.getProductId());
            }
        }

        // Создание заказа
        Order order = Order.builder()
                .userId(request.getUserId())
                .status(OrderStatus.NEW)
                .totalAmount(BigDecimal.ZERO)
                .build();

        List<OrderItem> items = request.getItems().stream()
                .map(i -> OrderItem.builder()
                        .productId(i.getProductId())
                        .productName(i.getProductName())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .order(order)
                        .build())
                .toList();

        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        Order saved = repository.save(order);

        // Резервирование товаров
        for (OrderItemRequest item : request.getItems()) {
            inventoryClient.reserveStock(item.getProductId(), item.getQuantity());
        }

        // Отправка в Kafka
        orderProducer.sendOrderCreated(OrderEvent.newBuilder()
                .setOrderId(saved.getId())
                .setUserId(saved.getUserId())
                .setStatus(saved.getStatus().name())
                .setTotalAmount(saved.getTotalAmount().toString())
                .setTimestamp(LocalDateTime.now().toString())
                .build());

        return toResponse(saved);
    }

    public List<OrderResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse getById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, String status) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return toResponse(repository.save(order));
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems().stream()
                .map(i -> OrderItemResponse.builder()
                        .productId(i.getProductId())
                        .productName(i.getProductName())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(o.getId())
                .userId(o.getUserId())
                .status(o.getStatus().name())
                .totalAmount(o.getTotalAmount())
                .createdAt(o.getCreatedAt())
                .items(items)
                .build();
    }
}
