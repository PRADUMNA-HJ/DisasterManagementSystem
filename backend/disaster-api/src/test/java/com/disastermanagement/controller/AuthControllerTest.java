package com.disastermanagement.controller;

import com.disastermanagement.config.SecurityConfig;
import com.disastermanagement.dto.AuthRequest;
import com.disastermanagement.dto.AuthResponse;
import com.disastermanagement.dto.RegisterRequest;
import com.disastermanagement.exception.GlobalExceptionHandler;
import com.disastermanagement.security.CustomUserDetailsService;
import com.disastermanagement.security.JwtService;
import com.disastermanagement.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("POST /api/v1/auth/register should create user and return 201 Created with JWT")
    void shouldRegisterSuccessfully() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Test Citizen")
                .email("citizen@example.com")
                .password("password123")
                .phone("+91-9876543210")
                .city("Bangalore")
                .role("CITIZEN")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("jwt.mock.token")
                .tokenType("Bearer")
                .userId(1L)
                .email("citizen@example.com")
                .fullName("Test Citizen")
                .role("CITIZEN")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt.mock.token"))
                .andExpect(jsonPath("$.data.email").value("citizen@example.com"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should authenticate and return 200 OK with JWT")
    void shouldLoginSuccessfully() throws Exception {
        AuthRequest request = AuthRequest.builder()
                .email("citizen@example.com")
                .password("password123")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("jwt.mock.token")
                .tokenType("Bearer")
                .userId(1L)
                .email("citizen@example.com")
                .fullName("Test Citizen")
                .role("CITIZEN")
                .build();

        when(authService.login(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt.mock.token"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 401 Unauthorized on invalid credentials")
    void shouldReturn401OnBadCredentials() throws Exception {
        AuthRequest request = AuthRequest.builder()
                .email("citizen@example.com")
                .password("wrongpassword")
                .build();

        when(authService.login(any(AuthRequest.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}
