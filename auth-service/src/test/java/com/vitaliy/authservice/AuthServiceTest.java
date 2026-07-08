package com.vitaliy.authservice;

import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.vitaliy.authservice.TestData.*;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void shouldRegisterUser(){

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("123456");

        UserResponse userResponse = authService.register(registerRequest);

        assertThat(userResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(userResponse.getRole()).isEqualTo("USER");
    }
    @Test
    void shouldLoginUser(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("123456");

        AuthResponse auth = authService.login(loginRequest);

        assertThat(auth.getAccessToken()).isNotBlank();
        assertThat(auth.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("wrong@test.com");
        reg.setPassword("123456");
        authService.register(reg);

        LoginRequest login = new LoginRequest();
        login.setEmail("wrong@test.com");
        login.setPassword("WRONG");

        assertThrows(RuntimeException.class, () -> authService.login(login));
    }

    @Test
    void contextLoads() {
        System.out.println("Spring context started");
    }
}
