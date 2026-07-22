package com.vitaliy.authservice.configForTest;

import com.vitaliy.authservice.repository.RefreshTokenRepository;
import com.vitaliy.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanDatabase() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }
}