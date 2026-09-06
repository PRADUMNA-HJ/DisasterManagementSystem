# Phase-05: Spring MVC & REST Controller Architecture

## Objectives
- Master Spring MVC Model-View-Controller pattern applied to RESTful web services.
- Design HTTP REST controllers handling request routing, query parameters, path variables, and request bodies.
- Implement strict DTO payload validation using Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Size`, `@Min`).
- Build standard HTTP response wrappers with explicit status codes (`200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`).

## Topics
1. **Spring MVC Architecture**:
   - `DispatcherServlet`: Front Controller pattern intercepting HTTP requests.
   - `HandlerMapping`: Resolves request URL to target controller method.
   - `HttpMessageConverter`: Deserializes incoming JSON to Java DTOs and serializes Java response objects to JSON (via Jackson).
2. **REST Stereotypes & Mapping Annotations**:
   - `@RestController`: Combines `@Controller` and `@ResponseBody`.
   - `@RequestMapping("/api/v1/...")`: Class-level base path definition.
   - `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`.
   - `@PathVariable`, `@RequestParam`, `@RequestBody`, `@RequestHeader`.
3. **Payload Validation with Jakarta Bean Validation**:
   - Annotations: `@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min`, `@Max`, `@Pattern`.
   - `@Valid` / `@Validated` parameter validation trigger in controller methods.
4. **HTTP Status Code Management**:
   - Returning `ResponseEntity<T>` with explicit status codes, headers, and body objects.

## Mini Project
Develop REST Controllers for Authentication, Disaster Reports, Emergency Shelters, Resource Requests, Incident Logs, and User Management with complete DTO mapping and validation.

## Integration into Disaster Management System
Location: `backend/disaster-api/src/main/java/com/disastermanagement/controller/`
- `AuthController`: Handles user registration (`POST /api/v1/auth/register`), authentication (`POST /api/v1/auth/login`), profile retrieval (`GET /api/v1/auth/me`).
- `DisasterController`: Manages disaster report CRUD, pagination, location search, and severity filtering (`/api/v1/disasters`).
- `ShelterController`: Manages shelter facility CRUD, pagination, and city search (`/api/v1/shelters`).
- `ResourceRequestController`: Handles relief resource submissions, status updates, and filtering (`/api/v1/resource-requests`).
- `IncidentLogController`: Records and retrieves disaster audit timelines (`/api/v1/incident-logs`).
- `UserController`: Admin user management endpoints (`/api/v1/users`).

## Git Commit
`feat(mvc): implement REST Controllers, DTO request/response payload bindings, and Jakarta validation`

## Interview Questions
1. *What is the role of `DispatcherServlet` in Spring MVC?*
   - `DispatcherServlet` acts as the Front Controller, receiving all incoming HTTP requests, delegating them to appropriate `HandlerMapping` and `HandlerAdapter` components, executing controllers, and rendering responses via message converters.
2. *How does `@Valid` work when annotated on a `@RequestBody` controller parameter?*
   - Spring invokes Hibernate Validator / Jakarta Validation before method execution. If validation fails, a `MethodArgumentNotValidException` is thrown and caught by `@RestControllerAdvice` to construct a 400 Bad Request error response.

## Completion Checklist
- [x] Implemented `@RestController` classes across all system modules
- [x] Applied DTO validation using `@Valid` and constraints (`@NotBlank`, `@Email`, etc.)
- [x] Constructed uniform `ResponseEntity` HTTP status codes across all endpoints
- [x] Verified controller endpoints using MockMvc integration tests
