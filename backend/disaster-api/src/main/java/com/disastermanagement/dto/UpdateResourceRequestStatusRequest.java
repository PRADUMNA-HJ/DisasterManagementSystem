package com.disastermanagement.dto;

import com.disastermanagement.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for updating the status of an existing ResourceRequest (e.g., PENDING -> APPROVED -> DELIVERED).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateResourceRequestStatusRequest {

    @NotNull(message = "Request status is required")
    private RequestStatus status;
}
