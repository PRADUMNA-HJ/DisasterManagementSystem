package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.IncidentLogCreateRequest;
import com.disastermanagement.dto.IncidentLogResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.service.IncidentLogService;
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
 * REST Controller for managing incident log audit entries.
 */
@RestController
@RequestMapping("/api/v1/incident-logs")
@Tag(name = "Incident Logs", description = "Endpoints for logging field updates and chronological disaster response actions")
public class IncidentLogController {

    private final IncidentLogService incidentLogService;

    public IncidentLogController(IncidentLogService incidentLogService) {
        this.incidentLogService = incidentLogService;
    }

    @GetMapping("/disaster/{disasterReportId}")
    @Operation(summary = "Retrieve chronological incident logs for a disaster (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<IncidentLogResponse>>> getLogsByDisasterReportId(
            @PathVariable("disasterReportId") Long disasterReportId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<IncidentLogResponse> logs = incidentLogService.getLogsByDisasterReportId(disasterReportId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Incident logs retrieved successfully", logs));
    }

    @GetMapping("/disaster/{disasterReportId}/all")
    @Operation(summary = "Retrieve all chronological incident logs for a disaster (unpaginated list)")
    public ResponseEntity<ApiResponse<List<IncidentLogResponse>>> getLogsByDisasterReportIdList(
            @PathVariable("disasterReportId") Long disasterReportId) {
        List<IncidentLogResponse> logs = incidentLogService.getLogsByDisasterReportId(disasterReportId);
        return ResponseEntity.ok(ApiResponse.success("All incident logs retrieved successfully", logs));
    }

    @PostMapping
    @Operation(summary = "Record a new operational incident log update")
    public ResponseEntity<ApiResponse<IncidentLogResponse>> createIncidentLog(@Valid @RequestBody IncidentLogCreateRequest request) {
        IncidentLogResponse created = incidentLogService.createIncidentLog(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Incident log recorded successfully", created));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an incident log entry (Admin)")
    public ResponseEntity<ApiResponse<Void>> deleteIncidentLog(@PathVariable("id") Long id) {
        incidentLogService.deleteIncidentLog(id);
        return ResponseEntity.ok(ApiResponse.success("Incident log deleted successfully", null));
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
