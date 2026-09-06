package com.disastermanagement.service;

import com.disastermanagement.dto.UpdateUserRequest;
import com.disastermanagement.dto.UserRequest;
import com.disastermanagement.dto.UserResponse;
import com.disastermanagement.entity.Role;
import com.disastermanagement.entity.User;
import com.disastermanagement.enums.RoleType;
import com.disastermanagement.exception.DuplicateResourceException;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.RoleRepository;
import com.disastermanagement.repository.UserRepository;
import com.disastermanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private Role citizenRole;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        citizenRole = Role.builder().id(1L).name(RoleType.CITIZEN).build();
        sampleUser = User.builder()
                .id(1L)
                .fullName("Alice Smith")
                .email("alice@example.com")
                .password("encoded_pass")
                .phone("+91-9876543210")
                .city("Bangalore")
                .role(citizenRole)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        UserRequest request = UserRequest.builder()
                .fullName("Alice Smith")
                .email("alice@example.com")
                .password("plain_pass")
                .phone("+91-9876543210")
                .city("Bangalore")
                .roleId(1L)
                .build();

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(citizenRole));
        when(passwordEncoder.encode("plain_pass")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponse response = userService.createUser(request);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("alice@example.com");
        assertThat(response.getRoleName()).isEqualTo("CITIZEN");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email is already registered")
    void shouldThrowWhenEmailAlreadyExists() {
        UserRequest request = UserRequest.builder()
                .fullName("Alice Smith")
                .email("alice@example.com")
                .password("plain_pass")
                .phone("+91-9876543210")
                .city("Bangalore")
                .roleId(1L)
                .build();

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("User with email alice@example.com already exists");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when role does not exist")
    void shouldThrowWhenRoleNotFound() {
        UserRequest request = UserRequest.builder()
                .fullName("Alice Smith")
                .email("alice@example.com")
                .password("plain_pass")
                .phone("+91-9876543210")
                .city("Bangalore")
                .roleId(99L)
                .build();

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        UpdateUserRequest updateReq = UpdateUserRequest.builder()
                .fullName("Alice Updated")
                .email("alice@example.com")
                .phone("+91-9876543210")
                .city("Chennai")
                .roleId(1L)
                .enabled(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(sampleUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(citizenRole));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponse response = userService.updateUser(1L, updateReq);

        assertThat(response).isNotNull();
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    @DisplayName("Should delete user when exists")
    void shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
