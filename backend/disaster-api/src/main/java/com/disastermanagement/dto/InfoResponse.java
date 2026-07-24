package com.disastermanagement.dto;

public class InfoResponse {

    private String application;
    private String status;
    private String version;

    public InfoResponse() {
    }

    public InfoResponse(String application, String status, String version) {
        this.application = application;
        this.status = status;
        this.version = version;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
