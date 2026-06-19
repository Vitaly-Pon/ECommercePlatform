package com.vitaliy.authservice.controller;

import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
