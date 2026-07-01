package com.vitaliy.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;

    private String userId;
    private String email;
    private String subject;
    private String message;
    private String type;        // ORDER_CREATED, ORDER_STATUS_CHANGED, etc.
    private Long orderId;
    private String status;      // SENT, PENDING, FAILED
    private LocalDateTime createdAt;
}
