# Coding Standards & Guidelines

## 1. Java Code Style & Formatting
- **Indent**: 4 spaces (no tabs).
- **Naming Conventions**:
  - Packages: `lowercase` (`com.disastermanagement.controller`)
  - Classes/Interfaces/Enums: `PascalCase` (`DisasterReportService`, `RoleType`)
  - Methods/Variables: `camelCase` (`getShelterById`, `reportedAt`)
  - Constants: `UPPER_SNAKE_CASE` (`MAX_SHELTER_CAPACITY`)

## 2. Spring & API Conventions
- **Constructor Injection**: Always use constructor injection for bean dependencies (leveraging `@RequiredArgsConstructor`). Avoid `@Autowired` on private fields.
- **REST Endpoints**: Use lower-case plural nouns for endpoint URIs (`/api/v1/disasters`, `/api/v1/shelters`).
- **HTTP Methods**:
  - `GET`: Read operations (safe & idempotent).
  - `POST`: Create resource.
  - `PUT`: Complete update of resource.
  - `PATCH`: Partial update.
  - `DELETE`: Remove resource.

## 3. Exception Handling
- Throw domain-specific runtime exceptions (`ResourceNotFoundException`, `ResourceConflictException`).
- Catch exceptions globally using `@RestControllerAdvice` (`GlobalExceptionHandler.java`) and return consistent `ApiErrorResponse` JSON.

## 4. DTO & Entity Separation
- Entities (`@Entity`) must NEVER be exposed directly in API requests/responses.
- Use explicit Request DTOs (`UserRegisterRequest`, `DisasterReportCreateRequest`) and Response DTOs (`UserResponse`, `DisasterReportResponse`).
