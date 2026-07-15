package com.vitaliy.authservice.support;

import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;

import java.math.BigDecimal;

public final class TestData {

    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "123456";

    public static final String ACCESS_TOKEN = "token123";
    public static final String REFRESH_TOKEN = "refresh456";


    public static RegisterRequest registerRequest() {
        return registerRequest(EMAIL, PASSWORD);
    }


    public static RegisterRequest registerRequest(String email, String password) {

        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);

        return request;
    }


    public static LoginRequest loginRequest() {
        return loginRequest(EMAIL, PASSWORD);
    }


    public static LoginRequest loginRequest(String email, String password) {

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        return request;
    }


    public static UserResponse userResponse() {

        return new UserResponse(
                1L,
                EMAIL,
                "USER"
        );
    }


    public static AuthResponse authResponse() {

        return new AuthResponse(
                ACCESS_TOKEN,
                REFRESH_TOKEN,
                "Bearer",
                3600000L,
                userResponse()
        );
    }


    private TestData() {
    }
}
