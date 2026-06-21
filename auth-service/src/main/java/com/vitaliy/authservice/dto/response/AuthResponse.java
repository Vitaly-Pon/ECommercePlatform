package com.vitaliy.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private Long userId;
    private String email;
    private String role;
}
