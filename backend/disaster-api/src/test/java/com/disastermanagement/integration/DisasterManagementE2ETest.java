package com.disastermanagement.integration;

import com.disastermanagement.dto.*;
import com.disastermanagement.entity.Role;
import com.disastermanagement.enums.RequestStatus;
import com.disastermanagement.enums.ResourceType;
import com.disastermanagement.enums.RoleType;
import com.disastermanagement.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DisasterManagementE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUpRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (roleRepository.findByName(roleType).isEmpty()) {
                roleRepository.save(new Role(roleType));
            }
        }
    }

    @Test
    @DisplayName("End-to-End Workflow: Register -> Login -> Report Disaster -> Request Resources -> Query")
    void shouldExecuteFullDisasterManagementLifecycle() throws Exception {
        // 1. Register a new citizen
        RegisterRequest registerReq = RegisterRequest.builder()
                .fullName("Citizen Vikram")
                .email("vikram.citizen@example.com")
                .password("password123")
                .phone("+91-9876543299")
                .city("Bangalore")
                .role("CITIZEN")
                .build();

        MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String regResponseBody = registerResult.getResponse().getContentAsString();
        AuthResponse citizenAuth = objectMapper.readTree(regResponseBody).get("data").traverse(objectMapper).readValueAs(AuthResponse.class);
        String citizenJwt = "Bearer " + citizenAuth.getToken();

        // 2. Report a new disaster
        CreateDisasterRequest disasterReq = CreateDisasterRequest.builder()
                .title("East Bangalore Flooding")
                .description("Submerged streets and stranded vehicles")
                .location("Bangalore East")
                .disasterType("FLOOD")
                .severity("HIGH")
                .build();

        MvcResult disasterResult = mockMvc.perform(post("/api/v1/disasters")
                        .header("Authorization", citizenJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disasterReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("East Bangalore Flooding"))
                .andReturn();

        String disasterBody = disasterResult.getResponse().getContentAsString();
        DisasterResponse createdDisaster = objectMapper.readTree(disasterBody).get("data").traverse(objectMapper).readValueAs(DisasterResponse.class);
        Long disasterId = createdDisaster.getId();

        // 3. Create a resource request for this disaster
        ResourceRequestCreateRequest resourceReq = ResourceRequestCreateRequest.builder()
                .resourceType(ResourceType.FOOD)
                .quantity(250)
                .userId(citizenAuth.getUserId())
                .disasterReportId(disasterId)
                .build();

        MvcResult resourceResult = mockMvc.perform(post("/api/v1/resource-requests")
                        .header("Authorization", citizenJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resourceReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.quantity").value(250))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andReturn();

        String resourceBody = resourceResult.getResponse().getContentAsString();
        ResourceRequestResponse createdResourceReq = objectMapper.readTree(resourceBody).get("data").traverse(objectMapper).readValueAs(ResourceRequestResponse.class);
        Long resourceReqId = createdResourceReq.getId();

        // 4. Retrieve paginated disasters
        mockMvc.perform(get("/api/v1/disasters?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        // 5. Query system info
        mockMvc.perform(get("/api/v1/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UP"));

        assertThat(disasterId).isNotNull();
        assertThat(resourceReqId).isNotNull();
    }
}
