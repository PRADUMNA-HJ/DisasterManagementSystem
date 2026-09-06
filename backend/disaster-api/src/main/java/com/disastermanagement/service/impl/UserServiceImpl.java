package com.disastermanagement.service.impl;

import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.UpdateUserRequest;
import com.disastermanagement.dto.UserRequest;
import com.disastermanagement.dto.UserResponse;
import com.disastermanagement.entity.Role;
import com.disastermanagement.entity.User;
import com.disastermanagement.exception.DuplicateResourceException;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.RoleRepository;
import com.disastermanagement.repository.UserRepository;
import com.disastermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserService for user operations.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all registered users");
        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> getAllUsers(Pageable pageable) {
        log.debug("Fetching paginated users: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<User> page = userRepository.findAll(pageable);
        List<UserResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        return userRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserResponseById(Long id) {
        log.debug("Fetching required user by id: {}", id);
        return userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByCity(String city) {
        log.debug("Fetching users by city: {}", city);
        return userRepository.findByCityIgnoreCase(city).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> getUsersByCity(String city, Pageable pageable) {
        log.debug("Fetching paginated users by city: {}", city);
        Page<User> page = userRepository.findByCityIgnoreCase(city, pageable);
        List<UserResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

        String encodedPassword = passwordEncoder != null
                ? passwordEncoder.encode(request.getPassword())
                : request.getPassword();

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(encodedPassword)
                .phone(request.getPhone())
                .city(request.getCity())
                .role(role)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Verify email uniqueness if changing email
        userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
            }
        });

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCity(request.getCity());
        user.setRole(role);
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id: {}", updatedUser.getId());
        return mapToResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .city(user.getCity())
                .enabled(user.isEnabled())
                .roleName(user.getRole() != null && user.getRole().getName() != null ? user.getRole().getName().name() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
