package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.UpdateDisasterRequest;
import com.disastermanagement.service.DisasterReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing disaster-related endpoints.
 * Operates purely as an HTTP transport layer.
 * Delegates all business operations to DisasterReportService.
 */
@RestController
@RequestMapping("/api/v1/disasters")
@Tag(name = "Disaster Reports", description = "Endpoints for reporting, viewing, searching, and managing disaster events")
public class DisasterController {

    private final DisasterReportService disasterReportService;

    public DisasterController(DisasterReportService disasterReportService) {
        this.disasterReportService = disasterReportService;
    }

    /**
     * Endpoint to retrieve disaster records with optional pagination.
     * GET /api/v1/disasters?page=0&size=20&sort=reportedAt,desc
     */
    @GetMapping
    @Operation(summary = "Retrieve all disaster records (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<DisasterResponse>>> getAllDisasters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<DisasterResponse> disasters = disasterReportService.getAllDisasters(pageable);
        return ResponseEntity.ok(ApiResponse.success("Disasters retrieved successfully", disasters));
    }

    /**
     * Endpoint to retrieve unpaginated list of all disasters (legacy support).
     * GET /api/v1/disasters/all
     */
    @GetMapping("/all")
    @Operation(summary = "Retrieve all disaster records (unpaginated list)")
    public ResponseEntity<ApiResponse<List<DisasterResponse>>> getAllDisastersList() {
        List<DisasterResponse> disasters = disasterReportService.getAllDisasters();
        return ResponseEntity.ok(ApiResponse.success("All disasters retrieved successfully", disasters));
    }

    /**
     * Endpoint to retrieve a specific disaster by its unique ID.
     * GET /api/v1/disasters/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve disaster details by ID")
    public ResponseEntity<ApiResponse<DisasterResponse>> getDisasterById(@PathVariable("id") Long id) {
        DisasterResponse disaster = disasterReportService.getDisasterResponseById(id);
        return ResponseEntity.ok(ApiResponse.success("Disaster retrieved successfully", disaster));
    }

    /**
     * Endpoint to search disasters filtered by location.
     * GET /api/v1/disasters/search?location=Bangalore&page=0&size=20
     */
    @GetMapping("/search")
    @Operation(summary = "Search disasters by location")
    public ResponseEntity<ApiResponse<PagedResponse<DisasterResponse>>> searchDisastersByLocation(
            @RequestParam(name = "location", required = false, defaultValue = "Bangalore") String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<DisasterResponse> filtered = disasterReportService.searchDisastersByLocation(location, pageable);
        return ResponseEntity.ok(ApiResponse.success("Disasters filtered by location successfully", filtered));
    }

    /**
     * Endpoint to filter disasters by severity level.
     * GET /api/v1/disasters/filter?severity=HIGH&page=0&size=20
     */
    @GetMapping("/filter")
    @Operation(summary = "Filter disasters by severity level")
    public ResponseEntity<ApiResponse<PagedResponse<DisasterResponse>>> filterDisastersBySeverity(
            @RequestParam(name = "severity", required = false, defaultValue = "HIGH") String severity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<DisasterResponse> filtered = disasterReportService.filterDisastersBySeverity(severity, pageable);
        return ResponseEntity.ok(ApiResponse.success("Disasters filtered by severity successfully", filtered));
    }

    /**
     * Endpoint to create/report a new disaster.
     * POST /api/v1/disasters
     */
    @PostMapping
    @Operation(summary = "Report a new disaster incident")
    public ResponseEntity<ApiResponse<DisasterResponse>> createDisaster(@Valid @RequestBody CreateDisasterRequest request) {
        DisasterResponse createdDisaster = disasterReportService.createDisaster(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Disaster reported successfully", createdDisaster));
    }

    /**
     * Endpoint to update an existing disaster report.
     * PUT /api/v1/disasters/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing disaster report")
    public ResponseEntity<ApiResponse<DisasterResponse>> updateDisaster(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateDisasterRequest request) {
        DisasterResponse updated = disasterReportService.updateDisaster(id, request);
        return ResponseEntity.ok(ApiResponse.success("Disaster updated successfully", updated));
    }

    /**
     * Endpoint to delete an existing disaster report.
     * DELETE /api/v1/disasters/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a disaster report")
    public ResponseEntity<ApiResponse<Void>> deleteDisaster(@PathVariable("id") Long id) {
        disasterReportService.deleteDisaster(id);
        return ResponseEntity.ok(ApiResponse.success("Disaster deleted successfully", null));
    }

    private Sort parseSort(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.by("id").descending();
        }
        if (sort.length >= 2) {
            return sort[1].equalsIgnoreCase("asc") ? Sort.by(sort[0]).ascending() : Sort.by(sort[0]).descending();
        }
        return Sort.by(sort[0]).descending();
    }
}
