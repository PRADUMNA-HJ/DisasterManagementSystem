package com.disastermanagement.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for returning User details in API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String city;
    private boolean enabled;
    private String roleName;
    private LocalDateTime createdAt;
}
