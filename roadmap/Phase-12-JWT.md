# Phase-12: JWT Stateless Authentication & Authorization

## Objectives
- Master JSON Web Token (JWT) structure: Header, Payload (Claims), Signature.
- Implement stateless token-based authentication using `jjwt` 0.12.6 library.
- Build `JwtService` for token generation, parsing, and validation.
- Build `JwtAuthenticationFilter` intercepting every HTTP request and populating the `SecurityContext`.

## Topics
1. **JWT Structure & Components**:
   - **Header**: Algorithm type (`alg: HS256`) and token type (`typ: JWT`).
   - **Payload (Claims)**: Subject (email), issued-at (`iat`), expiration (`exp`), custom claims.
   - **Signature**: HMAC-SHA256 of `base64(header) + "." + base64(payload)` signed with secret key.
   - Resulting format: `xxxxx.yyyyy.zzzzz` (Base64URL encoded parts joined by `.`).
2. **Token Lifecycle**:
   - **Login**: Client sends `email` + `password`. Server validates credentials, generates JWT, returns token.
   - **Request**: Client includes `Authorization: Bearer <token>` header in subsequent requests.
   - **Filter**: `JwtAuthenticationFilter` extracts, parses, validates token, loads `UserDetails`, and sets `SecurityContext`.
   - **Expiry**: Token expires after `expiration` milliseconds (e.g., 86400000ms = 24 hours).
3. **JJWT 0.12.6 API**:
   - `Jwts.builder()...signWith(key)...compact()`: Token generation.
   - `Jwts.parser().verifyWith(key).build().parseSignedClaims(token)`: Token parsing & signature validation.
4. **Stateless Authentication Benefits**:
   - No server-side session storage — horizontally scalable.
   - Token carries all claims needed for authorization decisions.

## Mini Project
Implement `JwtService.java` (token generation & validation), `JwtAuthenticationFilter.java` (per-request token verification), and wire into `SecurityConfig.java`.

## Integration into Disaster Management System
Location: `backend/disaster-api/src/main/java/com/disastermanagement/security/`
- `JwtService.java`: Generates tokens using HMAC-SHA256 secret (`application.security.jwt.secret-key`), extracts claims (`getUsername`, `isTokenValid`, `isTokenExpired`).
- `JwtAuthenticationFilter.java`: Extends `OncePerRequestFilter`, reads `Authorization` header, validates JWT, and sets `UsernamePasswordAuthenticationToken` in `SecurityContextHolder`.

## Git Commit
`feat(jwt): implement JwtService, JwtAuthenticationFilter, and stateless token authentication flow`

## Interview Questions
1. *What information is stored in a JWT payload and what should NOT be stored there?*
   - Payload contains: subject (user email/id), issued-at timestamp, expiration, and role claims. **Never store**: passwords, sensitive PII (SSN, credit cards), or secrets — JWT payloads are Base64-encoded, not encrypted, so anyone can decode them.
2. *What happens when a JWT expires and how do you handle token refresh?*
   - `JwtAuthenticationFilter` catches `ExpiredJwtException` thrown by the parser and delegates to `AuthenticationEntryPoint`, returning HTTP 401 Unauthorized. Token refresh is handled by a separate `/auth/refresh` endpoint exchanging a valid refresh token for a new access token.

## Completion Checklist
- [x] Implemented `JwtService` with HMAC-SHA256 token generation and claim extraction
- [x] Built `JwtAuthenticationFilter` extending `OncePerRequestFilter`
- [x] Registered filter before `UsernamePasswordAuthenticationFilter` in `SecurityFilterChain`
- [x] Configured JWT secret key and expiration via `application.properties`
