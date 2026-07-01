package com.vitaliy.notificationservice.service;

import com.vitaliy.notificationservice.domain.Notification;
import com.vitaliy.notificationservice.dto.NotificationResponse;
import com.vitaliy.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationResponse create(String userId, String email, String type,
                                       Long orderId, String subject, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .email(email)
                .type(type)
                .orderId(orderId)
                .subject(subject)
                .message(message)
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(repository.save(notification));
    }

    public List<NotificationResponse> getByUserId(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> getByOrderId(Long orderId) {
        return repository.findByOrderId(orderId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .email(n.getEmail())
                .subject(n.getSubject())
                .message(n.getMessage())
                .type(n.getType())
                .orderId(n.getOrderId())
                .status(n.getStatus())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
