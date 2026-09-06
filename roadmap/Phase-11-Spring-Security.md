# Phase-11: Spring Security 6 & Role-Based Access Control

## Objectives
- Master Spring Security 6 architecture: `SecurityFilterChain`, `AuthenticationManager`, `PasswordEncoder`, and `UserDetailsService`.
- Configure Role-Based Access Control (RBAC) using `@PreAuthorize`, `hasRole()`, and `hasAuthority()` expressions.
- Secure HTTP endpoints with method-level and URL-level access rules.
- Implement `BCryptPasswordEncoder` for secure credential storage.

## Topics
1. **Spring Security Architecture**:
   - `SecurityFilterChain`: Ordered filter pipeline processing every HTTP request.
   - `AuthenticationManager` & `AuthenticationProvider`: Delegates credential verification.
   - `UserDetailsService` / `CustomUserDetailsService`: Loads domain `User` from DB by username.
   - `SecurityContext` / `SecurityContextHolder`: Stores authenticated principal for request scope.
2. **Password Encoding with BCrypt**:
   - BCrypt is a one-way adaptive hashing function with built-in salting.
   - `BCryptPasswordEncoder.encode(rawPassword)` stores hashed credentials in DB.
   - `BCryptPasswordEncoder.matches(raw, hashed)` verifies login credentials.
3. **Role-Based Access Control (RBAC)**:
   - Roles defined in `roles` table (`ROLE_CITIZEN`, `ROLE_VOLUNTEER`, `ROLE_RESCUE_TEAM`, `ROLE_ADMIN`).
   - URL-level authorization: `requestMatchers("/api/v1/admin/**").hasRole("ADMIN")`.
   - Method-level authorization: `@PreAuthorize("hasRole('ADMIN') or hasRole('RESCUE_TEAM')")`.
4. **CORS Configuration**:
   - `CorsConfigurationSource` bean enabling cross-origin requests from React frontend.

## Mini Project
Configure `SecurityConfig.java` with `SecurityFilterChain` for HTTP security rules, disable CSRF (stateless REST), set `STATELESS` session policy, and configure BCrypt password encoding.

## Integration into Disaster Management System
Location: `backend/disaster-api/src/main/java/com/disastermanagement/config/SecurityConfig.java`
- Public endpoints: `/api/v1/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/h2-console/**`.
- Authenticated endpoints: `/api/v1/disasters`, `/api/v1/shelters`, `/api/v1/resource-requests`.
- Admin-only endpoints: `/api/v1/users/**`.

## Git Commit
`feat(security): configure Spring Security 6 filter chain, RBAC, BCrypt password encoding, and CORS`

## Interview Questions
1. *What is the Spring Security filter chain and how does it work?*
   - `SecurityFilterChain` is an ordered list of `Filter` implementations that every HTTP request passes through. Key filters include `CorsFilter`, `JwtAuthenticationFilter`, and `ExceptionTranslationFilter`. Filters are invoked in registration order before the request reaches a controller.
2. *Why is BCrypt preferred over SHA or MD5 for password hashing?*
   - BCrypt includes an embedded salt preventing rainbow table attacks, and uses a configurable cost factor (`strength`) making brute-force attacks computationally expensive as hardware improves.

## Completion Checklist
- [x] Configured `SecurityFilterChain` with `httpBasic` disabled and `SessionCreationPolicy.STATELESS`
- [x] Implemented `CustomUserDetailsService` loading `User` by email
- [x] Applied `@PreAuthorize` annotations to ADMIN and RESCUE_TEAM restricted endpoints
- [x] Configured BCrypt password encoder bean in `SecurityConfig`
