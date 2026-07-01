package com.vitaliy.notificationservice.controller;

import com.vitaliy.notificationservice.dto.NotificationResponse;
import com.vitaliy.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public List<NotificationResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<NotificationResponse> getByUserId(@PathVariable String userId) {
        return service.getByUserId(userId);
    }

    @GetMapping("/order/{orderId}")
    public List<NotificationResponse> getByOrderId(@PathVariable Long orderId) {
        return service.getByOrderId(orderId);
    }
}
