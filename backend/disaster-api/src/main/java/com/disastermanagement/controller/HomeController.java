package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.InfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Controller for providing API health and metadata information.
 * Follows RESTful naming conventions under the base path '/api/v1'.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "System Info", description = "System health, version, and operational metadata endpoints")
public class HomeController {

    /**
     * Endpoint to retrieve system information and operational status.
     * GET /api/v1/info
     */
    @GetMapping("/info")
    @Operation(summary = "Retrieve system health, version, and operational status")
    public ResponseEntity<ApiResponse<InfoResponse>> getInfo() {
        InfoResponse infoResponse = new InfoResponse(
                "Disaster Management System API",
                "1.0.0",
                "UP",
                LocalDateTime.now()
        );

        ApiResponse<InfoResponse> response = ApiResponse.success("System information retrieved successfully", infoResponse);
        return ResponseEntity.ok(response);
    }
}
