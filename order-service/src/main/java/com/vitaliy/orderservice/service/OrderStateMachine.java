package com.vitaliy.orderservice.service;
import com.vitaliy.orderservice.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class OrderStateMachine {

    private static final Map<OrderStatus, Set<OrderStatus>> TRANSITIONS = Map.of(
            OrderStatus.NEW,
            Set.of(OrderStatus.RESERVED, OrderStatus.CANCELLED),

            OrderStatus.RESERVED,
            Set.of(OrderStatus.PAID, OrderStatus.CANCELLED),

            OrderStatus.PAID,
            Set.of(OrderStatus.SHIPPED),

            OrderStatus.SHIPPED,
            Set.of(OrderStatus.COMPLETED),

            OrderStatus.COMPLETED,
            Set.of(),

            OrderStatus.CANCELLED,
            Set.of()
    );


    public boolean canChange(OrderStatus from, OrderStatus to) {
        return TRANSITIONS
                .getOrDefault(from, Set.of())
                .contains(to);
    }
}
