# Phase-06: REST API Architecture & Global Error Handling

## Objectives
- Master REST (Representational State Transfer) architectural constraints and endpoint design best practices.
- Implement standardized resource naming (`/api/v1/disasters`, `/api/v1/shelters`), HTTP verb semantics, and status code discipline.
- Build pagination and sorting response wrappers (`PagedResponse<T>`).
- Centralize exception handling using `@RestControllerAdvice` (`GlobalExceptionHandler`).
- Generate interactive OpenAPI 3.0 API documentation using Springdoc Swagger UI.

## Topics
1. **REST Architecture & Resource Naming**:
   - Statelessness, Client-Server separation, Uniform Interface, Cacheability.
   - Resource URIs using plural nouns (`/api/v1/resource-requests`).
   - HTTP Verbs: `GET` (read), `POST` (create), `PUT` (replace), `PATCH` (update), `DELETE` (remove).
2. **Pagination & Sorting**:
   - Query parameters: `page` (0-indexed), `size` (page capacity), `sortBy` (field name), `sortDir` (`asc` / `desc`).
   - `PagedResponse<T>` metadata envelope containing `content`, `pageNo`, `pageSize`, `totalElements`, `totalPages`, `last`.
3. **Global Exception Handling**:
   - `@RestControllerAdvice` and `@ExceptionHandler` annotations.
   - Uniform error response schema: `timestamp`, `status`, `error`, `message`, `path`.
   - Handling validation errors (`MethodArgumentNotValidException`), domain exceptions (`ResourceNotFoundException`, `ResourceConflictException`), authentication failures (`BadCredentialsException`), and access denied (`AccessDeniedException`).
4. **OpenAPI / Swagger Integration**:
   - `springdoc-openapi-starter-webmvc-ui` configuration (`OpenApiConfig.java`).
   - Interactive UI accessible at `/swagger-ui/index.html` and raw JSON at `/v3/api-docs`.

## Mini Project
Implement full REST API layer in `backend/disaster-api` complete with pagination, search/filtering endpoints, global exception advice, and Swagger documentation.

## Integration into Disaster Management System
Location: `backend/disaster-api`
- `GlobalExceptionHandler.java`: Intercepts all runtime exceptions and converts them to standardized `ApiErrorResponse` JSON.
- `PagedResponse.java`: Generic wrapper used across `DisasterController`, `ShelterController`, and `ResourceRequestController`.
- `OpenApiConfig.java`: Configures JWT Security Scheme in Swagger UI so developers can test authenticated endpoints directly in browser.

## Git Commit
`feat(rest): implement REST API standards, PagedResponse pagination wrappers, GlobalExceptionHandler, and Swagger UI`

## Interview Questions
1. *What makes an API RESTful?*
   - Adherence to REST constraints: Stateless communication, uniform resource URIs, standard HTTP methods and status codes, and JSON/XML representation formatting.
2. *Why is a `@RestControllerAdvice` preferred over handling exceptions inside individual controllers?*
   - Promotes Single Responsibility Principle (SRP) and DRY (Don't Repeat Yourself) by centralizing exception handling across all controllers in a single class.

## Completion Checklist
- [x] Implemented standardized REST endpoints across all domain resources
- [x] Created `PagedResponse<T>` pagination wrapper for paginated endpoints
- [x] Configured `@RestControllerAdvice` in `GlobalExceptionHandler.java`
- [x] Integrated Springdoc Swagger UI at `/swagger-ui/index.html`
