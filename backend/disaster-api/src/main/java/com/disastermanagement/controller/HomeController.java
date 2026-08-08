package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.InfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Controller for providing API health and metadata information.
 * Follows RESTful naming conventions under the base path '/api/v1'.
 * Returns standardized ApiResponse<InfoResponse> generic response wrapped in ResponseEntity.
 */
@RestController
@RequestMapping("/api/v1")
public class HomeController {

    /**
     * Endpoint to retrieve system information and operational status.
     * GET /api/v1/info
     * 
     * @return ResponseEntity wrapping ApiResponse containing InfoResponse details with HTTP 200 OK status.
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<InfoResponse>> getInfo() {
        // Construct system metadata DTO instance
        InfoResponse infoResponse = new InfoResponse(
                "Disaster Management System API",
                "1.0.0",
                "UP",
                LocalDateTime.now()
        );

        // Wrap metadata DTO in generic ApiResponse success wrapper
        ApiResponse<InfoResponse> response = ApiResponse.success("System information retrieved successfully", infoResponse);

        // Return HTTP 200 OK status containing wrapped response payload
        return ResponseEntity.ok(response);
    }
}
