package com.disastermanagement.controller;

import com.disastermanagement.dto.ApiResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.UpdateUserRequest;
import com.disastermanagement.dto.UserRequest;
import com.disastermanagement.dto.UserResponse;
import com.disastermanagement.service.UserService;
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
 * REST Controller for managing user operations.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Administrative endpoints for managing system users, profiles, and roles")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all registered users (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieve all registered users (unpaginated list)")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsersList() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a user by their unique ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("id") Long id) {
        UserResponse user = userService.getUserResponseById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by city (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> getUsersByCity(
            @RequestParam("city") String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortObj);

        PagedResponse<UserResponse> users = userService.getUsersByCity(city, pageable);
        return ResponseEntity.ok(ApiResponse.success("Users filtered by city successfully", users));
    }

    @PostMapping
    @Operation(summary = "Create a new user account (Admin)")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user's details (Admin)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updated = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user account (Admin)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
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
