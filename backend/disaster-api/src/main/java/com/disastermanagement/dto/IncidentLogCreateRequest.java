package com.disastermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for creating an IncidentLog entry.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentLogCreateRequest {

    @NotBlank(message = "Log message is required")
    private String message;

    @NotNull(message = "Disaster report ID is required")
    private Long disasterReportId;
}
