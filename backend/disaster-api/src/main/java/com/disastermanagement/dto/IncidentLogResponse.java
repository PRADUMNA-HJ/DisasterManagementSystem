package com.disastermanagement.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for returning IncidentLog data in API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentLogResponse {

    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private Long disasterReportId;
    private String disasterTitle;
}
