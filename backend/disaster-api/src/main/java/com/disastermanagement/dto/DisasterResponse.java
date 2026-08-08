package com.disastermanagement.dto;

import java.time.LocalDateTime;

public class DisasterResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String disasterType;
    private String severity;
    private LocalDateTime reportedAt;

    public DisasterResponse() {
    }

    public DisasterResponse(Long id, String title, String description, String location, String disasterType, String severity, LocalDateTime reportedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.disasterType = disasterType;
        this.severity = severity;
        this.reportedAt = reportedAt;
    }

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

