package com.disastermanagement.service;

import com.disastermanagement.dto.AuthRequest;
import com.disastermanagement.dto.AuthResponse;
import com.disastermanagement.dto.RegisterRequest;
import com.disastermanagement.dto.UserResponse;

/**
 * Service interface for authentication and registration workflows.
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);

    UserResponse getCurrentUser(String email);
}
