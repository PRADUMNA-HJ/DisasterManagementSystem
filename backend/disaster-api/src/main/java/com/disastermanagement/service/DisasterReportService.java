package com.disastermanagement.service;

import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.UpdateDisasterRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface defining business contracts for disaster report operations.
 * Decouples the HTTP presentation layer from business logic execution.
 */
public interface DisasterReportService {

    /**
     * Retrieve all disaster records without pagination.
     */
    List<DisasterResponse> getAllDisasters();

    /**
     * Retrieve all disaster records with pagination and sorting.
     */
    PagedResponse<DisasterResponse> getAllDisasters(Pageable pageable);

    /**
     * Retrieve a specific disaster record by its unique ID (Optional return).
     */
    Optional<DisasterResponse> getDisasterById(Long id);

    /**
     * Retrieve a specific disaster record by its unique ID, throwing ResourceNotFoundException if missing.
     */
    DisasterResponse getDisasterResponseById(Long id);

    /**
     * Search disaster records filtered by location without pagination.
     */
    List<DisasterResponse> searchDisastersByLocation(String location);

    /**
     * Search disaster records filtered by location with pagination.
     */
    PagedResponse<DisasterResponse> searchDisastersByLocation(String location, Pageable pageable);

    /**
     * Filter disaster records by severity level without pagination.
     */
    List<DisasterResponse> filterDisastersBySeverity(String severity);

    /**
     * Filter disaster records by severity level with pagination.
     */
    PagedResponse<DisasterResponse> filterDisastersBySeverity(String severity, Pageable pageable);

    /**
     * Process and record a new disaster report.
     */
    DisasterResponse createDisaster(CreateDisasterRequest request);

    /**
     * Update an existing disaster report with new details.
     */
    DisasterResponse updateDisaster(Long id, UpdateDisasterRequest request);

    /**
     * Delete an existing disaster report by its unique ID.
     */
    void deleteDisaster(Long id);
}
