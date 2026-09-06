package com.disastermanagement.dto;

import com.disastermanagement.enums.RequestStatus;
import com.disastermanagement.enums.ResourceType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for returning ResourceRequest data in API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceRequestResponse {

    private Long id;
    private ResourceType resourceType;
    private Integer quantity;
    private RequestStatus status;
    private LocalDateTime requestedAt;
    private Long userId;
    private String userName;
    private Long disasterReportId;
    private String disasterTitle;
}
