package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.ShelterRequest;
import com.disastermanagement.dto.ShelterResponse;
import com.disastermanagement.dto.UpdateShelterRequest;
import com.disastermanagement.service.ShelterService;
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
 * REST Controller for managing emergency shelter operations.
 */
@RestController
@RequestMapping("/api/v1/shelters")
@Tag(name = "Shelters", description = "Endpoints for finding, registering, and managing emergency relief shelters")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all emergency shelters (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<ShelterResponse>>> getAllShelters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<ShelterResponse> shelters = shelterService.getAllShelters(pageable);
        return ResponseEntity.ok(ApiResponse.success("Shelters retrieved successfully", shelters));
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieve all emergency shelters (unpaginated list)")
    public ResponseEntity<ApiResponse<List<ShelterResponse>>> getAllSheltersList() {
        List<ShelterResponse> shelters = shelterService.getAllShelters();
        return ResponseEntity.ok(ApiResponse.success("All shelters retrieved successfully", shelters));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve shelter details by ID")
    public ResponseEntity<ApiResponse<ShelterResponse>> getShelterById(@PathVariable("id") Long id) {
        ShelterResponse shelter = shelterService.getShelterResponseById(id);
        return ResponseEntity.ok(ApiResponse.success("Shelter retrieved successfully", shelter));
    }

    @GetMapping("/search")
    @Operation(summary = "Search shelters by city (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<ShelterResponse>>> getSheltersByCity(
            @RequestParam("city") String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<ShelterResponse> shelters = shelterService.getSheltersByCity(city, pageable);
        return ResponseEntity.ok(ApiResponse.success("Shelters filtered by city successfully", shelters));
    }

    @PostMapping
    @Operation(summary = "Register a new emergency shelter (Admin)")
    public ResponseEntity<ApiResponse<ShelterResponse>> createShelter(@Valid @RequestBody ShelterRequest request) {
        ShelterResponse created = shelterService.createShelter(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Shelter created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing shelter's details (Admin)")
    public ResponseEntity<ApiResponse<ShelterResponse>> updateShelter(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateShelterRequest request) {
        ShelterResponse updated = shelterService.updateShelter(id, request);
        return ResponseEntity.ok(ApiResponse.success("Shelter updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a shelter (Admin)")
    public ResponseEntity<ApiResponse<Void>> deleteShelter(@PathVariable("id") Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.ok(ApiResponse.success("Shelter deleted successfully", null));
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
