# Learning Notes: 06 - Spring MVC & REST Controller Architecture

## Overview
Spring MVC provides the web layer architecture for exposing RESTful web APIs. It uses the Front Controller pattern (`DispatcherServlet`) to route HTTP requests, bind data payloads, execute bean validation, and return JSON responses.

---

## 1. Request Lifecycle in Spring MVC

```text
HTTP Request (GET /api/v1/disasters?page=0&size=10)
    |
    v
DispatcherServlet (Front Controller)
    |
    v
HandlerMapping (Finds matching @RestController method)
    |
    v
HttpMessageConverter / Jackson (Deserializes JSON to Java DTO / Validates @Valid)
    |
    v
Controller Method Execution (@RestController -> Service Layer)
    |
    v
ResponseEntity<PagedResponse<DisasterReportResponse>> Returned
    |
    v
HttpMessageConverter (Serializes Java DTO to HTTP 200 JSON Response)
```

---

## 2. Controller Annotations & Endpoint Design

```java
@RestController
@RequestMapping("/api/v1/shelters")
@RequiredArgsConstructor
@Tag(name = "Shelter Management", description = "APIs for managing emergency shelters")
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping
    public ResponseEntity<PagedResponse<ShelterResponse>> getAllShelters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(shelterService.getAllShelters(page, size, sortBy, sortDir));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShelterResponse> createShelter(@Valid @RequestBody ShelterCreateRequest request) {
        return new ResponseEntity<>(shelterService.createShelter(request), HttpStatus.CREATED);
    }
}
```

---

## 3. Jakarta Bean Validation Annotations

Payload constraints enforced on Request DTOs before entering service logic:

```java
public class ShelterCreateRequest {

    @NotBlank(message = "Shelter name is required")
    @Size(min = 3, max = 150, message = "Name must be between 3 and 150 characters")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
}
```

---

## 4. Key Takeaways
- `@RestController` combines `@Controller` and `@ResponseBody` for JSON serialization.
- `@Valid` triggers declarative payload validation, delegating constraint errors to `GlobalExceptionHandler`.
- `ResponseEntity` ensures explicit control over HTTP status codes (`200 OK`, `201 Created`, `204 No Content`).
