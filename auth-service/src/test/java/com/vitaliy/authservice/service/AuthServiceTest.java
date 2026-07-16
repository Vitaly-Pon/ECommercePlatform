package com.vitaliy.authservice.service;

import com.vitaliy.authservice.configForTest.BaseIntegrationTest;
import com.vitaliy.authservice.config.security.JwtService;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.exeption.EmailAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.vitaliy.authservice.support.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthServiceTest  extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;


    @Autowired
    private JwtService jwtService;


    @Test
    void shouldRegisterUser() {

        UserResponse response =
                authService.register(registerRequest());

        assertThat(response.getEmail())
                .isEqualTo(EMAIL);

        assertThat(response.getRole())
                .isEqualTo("USER");
    }


    @Test
    void shouldLoginUser() {

        authService.register(registerRequest());

        AuthResponse response =
                authService.login(loginRequest());

        assertThat(response.getAccessToken())
                .isNotBlank();

        assertThat(response.getTokenType())
                .isEqualTo("Bearer");
    }

    @Test
    void shouldFailLoginWithWrongPassword() {

        authService.register(
                registerRequest("wrong@test.com", "123456")
        );

        assertThrows(
                RuntimeException.class,
                () -> authService.login(
                        loginRequest(
                                "wrong@test.com",
                                "WRONG"
                        )
                )
        );
    }

    @Test
    void shouldReturnFalseForInvalidToken() {

        boolean valid =
                jwtService.isTokenValid("bad.token.123456");

        assertThat(valid)
                .isFalse();
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        authService.register(registerRequest());

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(registerRequest())
        );
    }

    @Test
    void shouldRefreshToken() {
        authService.register(registerRequest());
        AuthResponse auth = authService.login(loginRequest());

        AuthResponse refreshed = authService.refresh(auth.getRefreshToken());

        assertThat(refreshed.getAccessToken()).isNotBlank();
    }

    @Test
    void shouldLogout() {
        authService.register(registerRequest());
        AuthResponse auth = authService.login(loginRequest());

        assertDoesNotThrow(() -> authService.logout(auth.getRefreshToken()));
    }
}