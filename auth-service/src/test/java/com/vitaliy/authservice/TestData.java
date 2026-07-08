package com.vitaliy.authservice;

import com.vitaliy.authservice.dto.request.RegisterRequest;

import java.math.BigDecimal;

public final class TestData {

    // Пользователи
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "123456";
    public static final String USER_ID = "1";

    // Товары
    public static final String PRODUCT_ID = "prod-123";
    public static final String PRODUCT_NAME = "iPhone";
    public static final BigDecimal PRODUCT_PRICE = new BigDecimal("999.99");

    // JSON-тела запросов
    public static final String REGISTER_JSON = """
            {"email":"test@test.com", "password":"123456"}
            """;

    // Методы-помощники
    public static RegisterRequest createRegisterRequest() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail(EMAIL);
        req.setPassword(PASSWORD);
        return req;
    }

    private TestData() {}
}
