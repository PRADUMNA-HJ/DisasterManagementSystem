package com.disastermanagement.enums;

/**
 * Enumeration tracking the lifecycle status of relief resource requests.
 */
public enum RequestStatus {
    /**
     * Request submitted and awaiting administrative/volunteer review.
     */
    PENDING,

    /**
     * Request reviewed and approved for fulfillment and dispatch.
     */
    APPROVED,

    /**
     * Requested resources successfully delivered to destination.
     */
    DELIVERED
}
