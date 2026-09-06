package com.disastermanagement.controller;

import com.disastermanagement.config.SecurityConfig;
import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.exception.GlobalExceptionHandler;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.security.CustomUserDetailsService;
import com.disastermanagement.security.JwtService;
import com.disastermanagement.service.DisasterReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisasterController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
class DisasterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DisasterReportService disasterReportService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("GET /api/v1/disasters should be publicly accessible and return 200 OK")
    void shouldGetAllDisastersPublicly() throws Exception {
        DisasterResponse response = new DisasterResponse(1L, "Urban Flood", "Description", "Bangalore", "FLOOD", "HIGH", LocalDateTime.now());
        PagedResponse<DisasterResponse> pagedResponse = PagedResponse.of(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        when(disasterReportService.getAllDisasters(any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/disasters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("Urban Flood"));
    }

    @Test
    @DisplayName("GET /api/v1/disasters/{id} should return 404 when not found")
    void shouldReturn404WhenDisasterNotFound() throws Exception {
        when(disasterReportService.getDisasterResponseById(99L))
                .thenThrow(new ResourceNotFoundException("DisasterReport", "id", 99L));

        mockMvc.perform(get("/api/v1/disasters/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("DisasterReport not found with id: '99'"));
    }

    @Test
    @WithMockUser(roles = "CITIZEN")
    @DisplayName("POST /api/v1/disasters should create disaster and return 201 Created")
    void shouldCreateDisasterWhenAuthenticated() throws Exception {
        CreateDisasterRequest request = CreateDisasterRequest.builder()
                .title("Urban Flood")
                .description("Heavy rain")
                .location("Bangalore")
                .disasterType("FLOOD")
                .severity("HIGH")
                .build();

        DisasterResponse response = new DisasterResponse(1L, "Urban Flood", "Heavy rain", "Bangalore", "FLOOD", "HIGH", LocalDateTime.now());
        when(disasterReportService.createDisaster(any(CreateDisasterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/disasters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Urban Flood"));
    }

    @Test
    @WithMockUser(roles = "CITIZEN")
    @DisplayName("POST /api/v1/disasters should return 400 Bad Request on validation failure")
    void shouldReturn400OnValidationFailure() throws Exception {
        CreateDisasterRequest invalidRequest = CreateDisasterRequest.builder()
                .title("") // Blank title violates @NotBlank
                .location("")
                .build();

        mockMvc.perform(post("/api/v1/disasters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.title").exists());
    }
}
