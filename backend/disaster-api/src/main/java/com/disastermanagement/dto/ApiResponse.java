package com.disastermanagement.dto;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper class to standardize responses across all endpoints in the application.
 * Encapsulates the execution status, a descriptive message, the response payload (data), and a timestamp.
 * 
 * @param <T> The type of data object returned in the response payload.
 */
public class ApiResponse<T> {

    // Flag indicating whether the API request was successful (true) or failed (false)
    private boolean success;

    // Human-readable message providing context about the request result
    private String message;

    // Generic response payload carrying domain objects, lists, or custom DTOs
    private T data;

    // Timestamp indicating when the API response was generated
    private LocalDateTime timestamp;

    /**
     * Default no-argument constructor required by JSON serialization/deserialization frameworks like Jackson.
     * Automatically sets the timestamp to the current system date and time upon initialization.
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Fully parameterized constructor for creating an ApiResponse instance with all fields explicitly set.
     * 
     * @param success   Status flag (true for success, false for failure)
     * @param message   Descriptive operation message
     * @param data      Payload data of generic type T
     * @param timestamp Custom timestamp of response creation
     */
    public ApiResponse(boolean success, String message, T data, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    /**
     * Convenient constructor for creating an ApiResponse instance with auto-generated timestamp.
     * 
     * @param success Status flag (true for success, false for failure)
     * @param message Descriptive operation message
     * @param data    Payload data of generic type T
     */
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Static helper method to construct a successful ApiResponse object with custom message and data.
     * 
     * @param <T>     Generic payload type
     * @param message Custom success message
     * @param data    Response payload data
     * @return Standardized successful ApiResponse wrapper instance
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Static helper method to construct a successful ApiResponse object with a default message and data.
     * 
     * @param <T>  Generic payload type
     * @param data Response payload data
     * @return Standardized successful ApiResponse wrapper instance
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation completed successfully", data);
    }

    /**
     * Static helper method to construct an error ApiResponse object with a failure message.
     * 
     * @param <T>     Generic payload type
     * @param message Descriptive error message
     * @return Standardized failure ApiResponse wrapper instance with null data payload
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    /**
     * Static helper method to construct an error ApiResponse object with a failure message and contextual error data.
     * 
     * @param <T>     Generic payload type
     * @param message Descriptive error message
     * @param data    Contextual error details or payload
     * @return Standardized failure ApiResponse wrapper instance
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }

    // Getter method for success field used by Jackson JSON serializer
    public boolean isSuccess() {
        return success;
    }

    // Setter method for success field used by Jackson JSON deserializer
    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter method for message field used by Jackson JSON serializer
    public String getMessage() {
        return message;
    }

    // Setter method for message field used by Jackson JSON deserializer
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter method for generic data field used by Jackson JSON serializer
    public T getData() {
        return data;
    }

    // Setter method for generic data field used by Jackson JSON deserializer
    public void setData(T data) {
        this.data = data;
    }

    // Getter method for timestamp field used by Jackson JSON serializer
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setter method for timestamp field used by Jackson JSON deserializer
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
