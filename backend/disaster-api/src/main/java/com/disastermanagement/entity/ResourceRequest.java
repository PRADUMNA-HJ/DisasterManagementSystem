package com.disastermanagement.entity;

import com.disastermanagement.enums.RequestStatus;
import com.disastermanagement.enums.ResourceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a request for disaster relief resources (food, water, medicine, clothing).
 * Mapped to 'resource_requests' table. Relates to both requesting User and associated DisasterReport.
 */
@Entity
@Table(name = "resource_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Resource type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 30)
    private ResourceType resourceType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Request status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestStatus status;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @NotNull(message = "Requesting user is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Disaster report is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_report_id", nullable = false)
    private DisasterReport disasterReport;

    @PrePersist
    protected void onCreate() {
        if (this.requestedAt == null) {
            this.requestedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
    }

    public ResourceRequest(ResourceType resourceType, Integer quantity, RequestStatus status, User user, DisasterReport disasterReport) {
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.status = status != null ? status : RequestStatus.PENDING;
        this.user = user;
        this.disasterReport = disasterReport;
        this.requestedAt = LocalDateTime.now();
    }
}
