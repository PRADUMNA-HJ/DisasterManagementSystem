package com.disastermanagement.dto;

import lombok.*;

/**
 * Response payload containing issued JWT token and authenticated user summary.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String email;
    private String fullName;
    private String role;
}
