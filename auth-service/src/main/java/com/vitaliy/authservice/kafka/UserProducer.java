package com.vitaliy.authservice.kafka;

import com.vitaliy.authservice.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProducer {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void sendUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user.created", event);
    }
}
