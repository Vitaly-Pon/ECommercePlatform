package com.vitaliy.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    @Test
    void contextLoads() {
        System.out.println("Spring context started");
    }
}
