package com.vitaliy.authservice.service;

import com.vitaliy.authservice.domain.User;
import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;

public interface AuthService {

   UserResponse register(RegisterRequest registerRequest);
   AuthResponse login(LoginRequest request);
}
