package com.disastermanagement.dto;

import com.disastermanagement.enums.ResourceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for creating a new ResourceRequest.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceRequestCreateRequest {

    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Disaster report ID is required")
    private Long disasterReportId;
}
