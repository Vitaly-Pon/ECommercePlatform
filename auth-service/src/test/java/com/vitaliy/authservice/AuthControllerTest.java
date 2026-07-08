package com.vitaliy.authservice;

import com.vitaliy.authservice.controller.AuthController;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static com.vitaliy.authservice.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private com.vitaliy.authservice.config.security.JwtService jwtService;

    @Test
    void shouldRegisterUser() throws Exception {
        when(authService.register(any()))
                .thenReturn(new UserResponse(1L, EMAIL, "USER"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType("application/json")
                        .content(REGISTER_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.role").value("USER"));

    }

    @Test
    void shouldReturn400WhenInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content("{\"email\":\"NOEMAIL\", \"password\":\"123456\"}"))  // плохой email
                .andExpect(status().isBadRequest());  // 400
    }
    @Test
    void shouldReturn400WhenPasswordIsBlank() throws Exception {

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content("{\"email\":\"" + EMAIL + "\", \"password\":\"\"}")) // пустой пароль
                .andExpect(status().isBadRequest());
    }


}
