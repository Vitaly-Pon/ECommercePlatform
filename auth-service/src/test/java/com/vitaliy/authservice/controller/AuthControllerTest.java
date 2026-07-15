package com.vitaliy.authservice.controller;

import com.vitaliy.authservice.config.security.JwtService;
import com.vitaliy.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.vitaliy.authservice.support.TestData.ACCESS_TOKEN;
import static com.vitaliy.authservice.support.TestData.authResponse;
import static com.vitaliy.authservice.support.TestData.userResponse;
import static com.vitaliy.authservice.support.TestRequests.EMPTY_EMAIL_JSON;
import static com.vitaliy.authservice.support.TestRequests.EMPTY_PASSWORD_JSON;
import static com.vitaliy.authservice.support.TestRequests.INVALID_EMAIL_JSON;
import static com.vitaliy.authservice.support.TestRequests.WRONG_PASSWORD_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static com.vitaliy.authservice.support.TestData.EMAIL;
import static com.vitaliy.authservice.support.TestRequests.REGISTER_JSON;
import static com.vitaliy.authservice.support.TestRequests.LOGIN_JSON;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    private static final String REGISTER_URL = "/api/v1/auth/register";
    private static final String LOGIN_URL = "/api/v1/auth/login";


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AuthService authService;


    @MockBean
    private JwtService jwtService;


    @Test
    void shouldRegisterUser() throws Exception {

        when(authService.register(any()))
                .thenReturn(userResponse());


        mockMvc.perform(post(REGISTER_URL)
                        .with(csrf())
                        .contentType("application/json")
                        .content(REGISTER_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.role").value("USER"));
    }


    @Test
    void shouldReturn400WhenInvalidEmail() throws Exception {

        mockMvc.perform(post(REGISTER_URL)
                        .contentType("application/json")
                        .content(INVALID_EMAIL_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldReturn400WhenPasswordIsBlank() throws Exception {

        mockMvc.perform(post(REGISTER_URL)
                        .contentType("application/json")
                        .content(EMPTY_PASSWORD_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldLoginUser() throws Exception {

        when(authService.login(any()))
                .thenReturn(authResponse());


        mockMvc.perform(post(LOGIN_URL)
                        .with(csrf())
                        .contentType("application/json")
                        .content(LOGIN_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }


    @Test
    void shouldReturn400WhenBadCredentials() throws Exception {

        when(authService.login(any()))
                .thenThrow(new RuntimeException("Invalid password"));


        mockMvc.perform(post(LOGIN_URL)
                        .with(csrf())
                        .contentType("application/json")
                        .content(WRONG_PASSWORD_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldReturn400WhenLoginEmailIsBlank() throws Exception {

        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(EMPTY_EMAIL_JSON))
                .andExpect(status().isBadRequest());
    }

}
