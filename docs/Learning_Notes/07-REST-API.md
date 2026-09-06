# Learning Notes: 07 - REST API Architecture, Pagination & Swagger Documentation

## Overview
Representational State Transfer (REST) is the architectural style used for designing web services in the **Disaster Management System**. RESTful APIs communicate over HTTP using standard verbs, resource URIs, stateless request headers, and JSON representation formatting.

---

## 1. REST Architectural Constraints

1. **Client-Server**: Separation of UI concerns from backend storage and business logic.
2. **Statelessness**: Every HTTP request from client to server must contain all necessary authentication and context data (JWT bearer token). No session state stored on server.
3. **Cacheability**: Responses explicitly declare cache controls where appropriate.
4. **Uniform Interface**: Resource URIs use standard nouns (`/api/v1/disasters`) and standard HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`).

---

## 2. Pagination & Sorting Implementation

To prevent memory overload when querying large disaster or shelter records, endpoints return a paginated envelope `PagedResponse<T>`:

```java
public class PagedResponse<T> {
    private List<T> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
```

### Controller Invocation Pattern:
```java
@GetMapping
public ResponseEntity<PagedResponse<DisasterReportResponse>> getAllDisasters(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sortBy", defaultValue = "reportedAt") String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
    return ResponseEntity.ok(disasterReportService.getAllDisasters(page, size, sortBy, sortDir));
}
```

---

## 3. Global Exception Handling Architecture

Centralized advice catches domain exceptions and returns structured `ApiErrorResponse` JSON with standard HTTP status codes:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
```

---

## 4. Key Takeaways
- Stateless REST APIs scale horizontally by avoiding server-side HTTP session state.
- Generic `PagedResponse<T>` provides consistent client metadata for pagination UI controls.
- Global exception advice prevents sensitive stack trace leaks while keeping error payloads uniform.
