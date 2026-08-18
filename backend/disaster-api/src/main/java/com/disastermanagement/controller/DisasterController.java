package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;
import com.disastermanagement.service.DisasterReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing disaster-related endpoints.
 * Operates purely as an HTTP transport layer.
 * Delegates all business operations to DisasterReportService.
 * Uses Constructor Injection to receive its dependencies.
 */
@RestController
@RequestMapping("/api/v1/disasters")
public class DisasterController {

    private final DisasterReportService disasterReportService;

    /**
     * Constructor Injection.
     * Spring automatically injects the DisasterReportService bean when instantiating DisasterController.
     * No @Autowired annotation required for single-constructor classes in modern Spring.
     *
     * @param disasterReportService Injected service dependency
     */
    public DisasterController(DisasterReportService disasterReportService) {
        this.disasterReportService = disasterReportService;
    }

    /**
     * Endpoint to retrieve all disaster records.
     * GET /api/v1/disasters
     * 
     * @return ResponseEntity wrapping ApiResponse containing the list of all disasters with HTTP 200 OK status.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DisasterResponse>>> getAllDisasters() {
        List<DisasterResponse> disasters = disasterReportService.getAllDisasters();
        ApiResponse<List<DisasterResponse>> response = ApiResponse.success("Disasters retrieved successfully", disasters);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve a specific disaster by its unique ID.
     * GET /api/v1/disasters/{id}
     * 
     * @param id Unique identifier of the disaster extracted from URL PathVariable
     * @return ResponseEntity wrapping ApiResponse with disaster details if found (HTTP 200 OK), or error message if not found (HTTP 404 NOT_FOUND).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DisasterResponse>> getDisasterById(@PathVariable("id") Long id) {
        return disasterReportService.getDisasterById(id)
                .map(disaster -> ResponseEntity.ok(ApiResponse.success("Disaster retrieved successfully", disaster)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Disaster not found with ID: " + id)));
    }

    /**
     * Endpoint to search disasters filtered by location.
     * GET /api/v1/disasters/search?location=Bangalore
     * 
     * @param location Query parameter specifying location to filter by (default value: "Bangalore")
     * @return ResponseEntity wrapping ApiResponse containing matching filtered disasters with HTTP 200 OK status.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DisasterResponse>>> searchDisastersByLocation(
            @RequestParam(name = "location", required = false, defaultValue = "Bangalore") String location) {
        List<DisasterResponse> filtered = disasterReportService.searchDisastersByLocation(location);
        ApiResponse<List<DisasterResponse>> response = ApiResponse.success("Disasters filtered by location successfully", filtered);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to filter disasters by severity level.
     * GET /api/v1/disasters/filter?severity=HIGH
     * 
     * @param severity Query parameter specifying severity to filter by (default value: "HIGH")
     * @return ResponseEntity wrapping ApiResponse containing matching filtered disasters with HTTP 200 OK status.
     */
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<DisasterResponse>>> filterDisastersBySeverity(
            @RequestParam(name = "severity", required = false, defaultValue = "HIGH") String severity) {
        List<DisasterResponse> filtered = disasterReportService.filterDisastersBySeverity(severity);
        ApiResponse<List<DisasterResponse>> response = ApiResponse.success("Disasters filtered by severity successfully", filtered);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to create/report a new disaster.
     * POST /api/v1/disasters
     * 
     * @param request JSON payload mapping to CreateDisasterRequest body
     * @return ResponseEntity wrapping ApiResponse with created disaster payload with HTTP 201 CREATED status.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DisasterResponse>> createDisaster(@RequestBody CreateDisasterRequest request) {
        DisasterResponse createdDisaster = disasterReportService.createDisaster(request);
        ApiResponse<DisasterResponse> response = ApiResponse.success("Disaster reported successfully", createdDisaster);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
