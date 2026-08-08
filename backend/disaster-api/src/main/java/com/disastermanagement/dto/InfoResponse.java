package com.disastermanagement.dto;

import java.time.LocalDateTime;

public class InfoResponse {

    private String application;
    private String version;
    private String status;
    private LocalDateTime timestamp;

    public InfoResponse() {
    }

    public InfoResponse(String application, String version, String status, LocalDateTime timestamp) {
        this.application = application;
        this.version = version;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
