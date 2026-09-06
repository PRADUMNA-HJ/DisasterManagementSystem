package com.disastermanagement.service;

import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.UpdateUserRequest;
import com.disastermanagement.dto.UserRequest;
import com.disastermanagement.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User management business operations.
 */
public interface UserService {

    List<UserResponse> getAllUsers();

    PagedResponse<UserResponse> getAllUsers(Pageable pageable);

    Optional<UserResponse> getUserById(Long id);

    UserResponse getUserResponseById(Long id);

    Optional<UserResponse> getUserByEmail(String email);

    List<UserResponse> getUsersByCity(String city);

    PagedResponse<UserResponse> getUsersByCity(String city, Pageable pageable);

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
}
