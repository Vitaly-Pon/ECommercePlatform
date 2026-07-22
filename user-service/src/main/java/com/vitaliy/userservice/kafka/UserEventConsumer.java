package com.vitaliy.userservice.kafka;

import com.vitaliy.authservice.event.UserCreatedEvent;
import com.vitaliy.userservice.domain.User;
import com.vitaliy.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final UserRepository userRepository;

    @KafkaListener(topics = "user.created", groupId = "user-group")
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("Received user.created: userId={}, email={}", event.getUserId(), event.getEmail());

        User user = User.builder()
                .id(event.getUserId())
                .email(event.getEmail().toString())
                .role(event.getRole().toString())
                .build();

        userRepository.save(user);
    }
}
