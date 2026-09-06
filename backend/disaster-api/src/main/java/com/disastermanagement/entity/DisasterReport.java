package com.disastermanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity representing the 'disaster_reports' database table.
 * Encapsulates domain model data for disaster records and maintains 1:M relationships with ResourceRequest & IncidentLog.
 */
@Entity
@Table(name = "disaster_reports")
public class DisasterReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotBlank(message = "Location is required")
    @Column(name = "location", nullable = false)
    private String location;

    @NotBlank(message = "Disaster type is required")
    @Column(name = "disaster_type", nullable = false)
    private String disasterType;

    @NotBlank(message = "Severity is required")
    @Column(name = "severity", nullable = false)
    private String severity;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @OneToMany(mappedBy = "disasterReport", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ResourceRequest> resourceRequests = new ArrayList<>();

    @OneToMany(mappedBy = "disasterReport", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<IncidentLog> incidentLogs = new ArrayList<>();

    /**
     * Default no-arg constructor required by JPA specification / Reflection API.
     */
    public DisasterReport() {
    }

    /**
     * Parameterized constructor without ID (for creation before persistence).
     */
    public DisasterReport(String title, String description, String location, String disasterType, String severity, LocalDateTime reportedAt) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.disasterType = disasterType;
        this.severity = severity;
        this.reportedAt = reportedAt != null ? reportedAt : LocalDateTime.now();
    }

    /**
     * Full parameterized constructor (with ID).
     */
    public DisasterReport(Long id, String title, String description, String location, String disasterType, String severity, LocalDateTime reportedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.disasterType = disasterType;
        this.severity = severity;
        this.reportedAt = reportedAt != null ? reportedAt : LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public List<ResourceRequest> getResourceRequests() {
        return resourceRequests;
    }

    public void setResourceRequests(List<ResourceRequest> resourceRequests) {
        this.resourceRequests = resourceRequests;
    }

    public List<IncidentLog> getIncidentLogs() {
        return incidentLogs;
    }

    public void setIncidentLogs(List<IncidentLog> incidentLogs) {
        this.incidentLogs = incidentLogs;
    }

    // Helper methods for cascading relationships
    public void addResourceRequest(ResourceRequest request) {
        resourceRequests.add(request);
        request.setDisasterReport(this);
    }

    public void removeResourceRequest(ResourceRequest request) {
        resourceRequests.remove(request);
        request.setDisasterReport(null);
    }

    public void addIncidentLog(IncidentLog log) {
        incidentLogs.add(log);
        log.setDisasterReport(this);
    }

    public void removeIncidentLog(IncidentLog log) {
        incidentLogs.remove(log);
        log.setDisasterReport(null);
    }
}
