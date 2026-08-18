package com.disastermanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing the 'disaster_reports' database table.
 * Encapsulates domain model data for disaster records.
 */
@Entity
@Table(name = "disaster_reports")
public class DisasterReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "disaster_type", nullable = false)
    private String disasterType;

    @Column(name = "severity", nullable = false)
    private String severity;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

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
        this.reportedAt = reportedAt;
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
        this.reportedAt = reportedAt;
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
}

