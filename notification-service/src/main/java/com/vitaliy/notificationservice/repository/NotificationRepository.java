package com.vitaliy.notificationservice.repository;

import com.vitaliy.notificationservice.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByOrderId(Long orderId);
}
