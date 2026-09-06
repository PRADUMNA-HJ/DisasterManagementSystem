package com.disastermanagement.enums;

/**
 * Enumeration representing user roles within the Disaster Management System.
 * Controls access permissions and responsibilities across the application.
 */
public enum RoleType {
    /**
     * Citizens who report incidents and request emergency aid.
     */
    CITIZEN,

    /**
     * Community volunteers registered to assist in rescue & relief operations.
     */
    VOLUNTEER,

    /**
     * Authorized emergency responders and field operations teams.
     */
    RESCUE_TEAM,

    /**
     * System administrators and government officials overseeing response workflows.
     */
    ADMIN
}
