package com.disastermanagement.service;

import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service interface defining business contracts for disaster report operations.
 * Decouples the HTTP presentation layer from business logic execution.
 */
public interface DisasterReportService {

    /**
     * Retrieve all disaster records.
     * 
     * @return List of all disaster responses.
     */
    List<DisasterResponse> getAllDisasters();

    /**
     * Retrieve a specific disaster record by its unique ID.
     * 
     * @param id Unique identifier of the disaster.
     * @return Optional containing DisasterResponse if found, or empty Optional if not found.
     */
    Optional<DisasterResponse> getDisasterById(Long id);

    /**
     * Search disaster records filtered by location.
     * 
     * @param location Location name to filter by.
     * @return List of disaster responses matching the specified location.
     */
    List<DisasterResponse> searchDisastersByLocation(String location);

    /**
     * Filter disaster records by severity level.
     * 
     * @param severity Severity level string to filter by.
     * @return List of disaster responses matching the specified severity.
     */
    List<DisasterResponse> filterDisastersBySeverity(String severity);

    /**
     * Process and record a new disaster report.
     * 
     * @param request Data transfer object containing disaster creation details.
     * @return DisasterResponse containing created disaster information.
     */
    DisasterResponse createDisaster(CreateDisasterRequest request);
}
