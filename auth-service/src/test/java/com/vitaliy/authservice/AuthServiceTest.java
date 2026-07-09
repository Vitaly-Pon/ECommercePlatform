package com.vitaliy.authservice;

import com.vitaliy.authservice.config.security.JwtService;
import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.exeption.EmailAlreadyExistsException;
import com.vitaliy.authservice.repository.RefreshTokenRepository;
import com.vitaliy.authservice.repository.UserRepository;
import com.vitaliy.authservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser(){

        RegisterRequest reg = new RegisterRequest();
        reg.setEmail(EMAIL);
        reg.setPassword(PASSWORD);

        UserResponse userResponse = authService.register(reg);

        assertThat(userResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(userResponse.getRole()).isEqualTo("USER");
    }

    @Test
    void shouldLoginUser(){
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail(EMAIL);
        reg.setPassword(PASSWORD);
        authService.register(reg);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

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
    void shouldReturnFalseForInvalidToken() {
        boolean valid = jwtService.isTokenValid("bad.token.123456");
        assertThat(valid).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Первая регистрация
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail(EMAIL);
        reg.setPassword(PASSWORD);
        authService.register(reg);

        // Вторая регистрация с тем же email
        assertThrows(EmailAlreadyExistsException.class, () -> {
            RegisterRequest duplicate = new RegisterRequest();
            duplicate.setEmail(EMAIL);
            duplicate.setPassword("123123");
            authService.register(duplicate);
        });
    }

}
