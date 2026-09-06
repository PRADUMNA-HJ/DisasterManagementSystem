package com.disastermanagement.service;

import com.disastermanagement.dto.AuthRequest;
import com.disastermanagement.dto.AuthResponse;
import com.disastermanagement.dto.RegisterRequest;
import com.disastermanagement.entity.Role;
import com.disastermanagement.entity.User;
import com.disastermanagement.enums.RoleType;
import com.disastermanagement.exception.DuplicateResourceException;
import com.disastermanagement.repository.RoleRepository;
import com.disastermanagement.repository.UserRepository;
import com.disastermanagement.security.CustomUserDetails;
import com.disastermanagement.security.JwtService;
import com.disastermanagement.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private Role citizenRole;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        citizenRole = Role.builder().id(1L).name(RoleType.CITIZEN).build();
        sampleUser = User.builder()
                .id(1L)
                .fullName("Jane Doe")
                .email("jane@example.com")
                .password("encoded_pass")
                .phone("+91-9876543210")
                .city("Bangalore")
                .role(citizenRole)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should register new user and return JWT token")
    void shouldRegisterNewUser() {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Jane Doe")
                .email("jane@example.com")
                .password("secret123")
                .phone("+91-9876543210")
                .city("Bangalore")
                .role("CITIZEN")
                .build();

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(roleRepository.findByName(RoleType.CITIZEN)).thenReturn(Optional.of(citizenRole));
        when(passwordEncoder.encode("secret123")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(response.getRole()).isEqualTo("CITIZEN");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on registration with existing email")
    void shouldThrowWhenRegisteringExistingEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Jane Doe")
                .email("jane@example.com")
                .password("secret123")
                .phone("+91-9876543210")
                .city("Bangalore")
                .build();

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Should login successfully and return JWT token")
    void shouldLoginSuccessfully() {
        AuthRequest request = AuthRequest.builder()
                .email("jane@example.com")
                .password("secret123")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(sampleUser));
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn("mock.jwt.token");

        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @DisplayName("Should propagate BadCredentialsException when login credentials fail")
    void shouldPropagateBadCredentials() {
        AuthRequest request = AuthRequest.builder()
                .email("jane@example.com")
                .password("wrong_password")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}
