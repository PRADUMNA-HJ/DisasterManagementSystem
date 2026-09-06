package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.ResourceRequestCreateRequest;
import com.disastermanagement.dto.ResourceRequestResponse;
import com.disastermanagement.dto.UpdateResourceRequestStatusRequest;
import com.disastermanagement.enums.RequestStatus;
import com.disastermanagement.service.ResourceRequestService;
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
 * REST Controller for managing disaster relief resource requests.
 */
@RestController
@RequestMapping("/api/v1/resource-requests")
@Tag(name = "Resource Requests", description = "Endpoints for requesting, tracking, and updating disaster relief resources")
public class ResourceRequestController {

    private final ResourceRequestService resourceRequestService;

    public ResourceRequestController(ResourceRequestService resourceRequestService) {
        this.resourceRequestService = resourceRequestService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all resource requests (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<ResourceRequestResponse>>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<ResourceRequestResponse> requests = resourceRequestService.getAllRequests(pageable);
        return ResponseEntity.ok(ApiResponse.success("Resource requests retrieved successfully", requests));
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieve all resource requests (unpaginated list)")
    public ResponseEntity<ApiResponse<List<ResourceRequestResponse>>> getAllRequestsList() {
        List<ResourceRequestResponse> requests = resourceRequestService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success("All resource requests retrieved successfully", requests));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a resource request by ID")
    public ResponseEntity<ApiResponse<ResourceRequestResponse>> getRequestById(@PathVariable("id") Long id) {
        ResourceRequestResponse request = resourceRequestService.getRequestById(id);
        return ResponseEntity.ok(ApiResponse.success("Resource request retrieved successfully", request));
    }

    @GetMapping("/status")
    @Operation(summary = "Filter resource requests by status (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<ResourceRequestResponse>>> getRequestsByStatus(
            @RequestParam("status") RequestStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<ResourceRequestResponse> requests = resourceRequestService.getRequestsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Resource requests filtered by status successfully", requests));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Retrieve resource requests submitted by a user (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<ResourceRequestResponse>>> getRequestsByUserId(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<ResourceRequestResponse> requests = resourceRequestService.getRequestsByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Resource requests for user retrieved successfully", requests));
    }

    @GetMapping("/disaster/{disasterReportId}")
    @Operation(summary = "Retrieve resource requests for a disaster report (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<ResourceRequestResponse>>> getRequestsByDisasterReportId(
            @PathVariable("disasterReportId") Long disasterReportId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<ResourceRequestResponse> requests = resourceRequestService.getRequestsByDisasterReportId(disasterReportId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Resource requests for disaster report retrieved successfully", requests));
    }

    @PostMapping
    @Operation(summary = "Submit a new relief resource request")
    public ResponseEntity<ApiResponse<ResourceRequestResponse>> createRequest(@Valid @RequestBody ResourceRequestCreateRequest request) {
        ResourceRequestResponse created = resourceRequestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource request created successfully", created));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update the status of a resource request (Admin, Rescue Team, Volunteer)")
    public ResponseEntity<ApiResponse<ResourceRequestResponse>> updateRequestStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateResourceRequestStatusRequest request) {
        ResourceRequestResponse updated = resourceRequestService.updateRequestStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Resource request status updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a resource request (Admin)")
    public ResponseEntity<ApiResponse<Void>> deleteRequest(@PathVariable("id") Long id) {
        resourceRequestService.deleteRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Resource request deleted successfully", null));
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
