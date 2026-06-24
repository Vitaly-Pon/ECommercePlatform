package com.vitaliy.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthServiceTest extends PostgresTestContainerConfig {

    @Test
    void contextLoads() {
        System.out.println("Spring context started");
    }
}
