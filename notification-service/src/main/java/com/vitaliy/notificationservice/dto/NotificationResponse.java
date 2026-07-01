package com.vitaliy.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private String id;
    private String userId;
    private String email;
    private String subject;
    private String message;
    private String type;
    private Long orderId;
    private String status;
    private LocalDateTime createdAt;
}
