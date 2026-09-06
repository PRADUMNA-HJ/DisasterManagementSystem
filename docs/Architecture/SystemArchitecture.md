# Disaster Management System - High-Level System Architecture

## Architecture Overview

The **Disaster Management System** is built following an enterprise **Layered MVC Architecture** (Modular Monolith) with strict separation of concerns across HTTP presentation, security filtering, business logic, persistence, and database layers.

```text
+-----------------------------------------------------------------------+
|                         HTTP Clients & Frontend                       |
|           (React Application / Postman / Swagger UI / Mobile)         |
+-----------------------------------------------------------------------+
                                    |
                                    v
+-----------------------------------------------------------------------+
|                    Security Filter Chain (Spring Security 6)          |
|      - CorsFilter (Cross-Origin Configuration)                        |
|      - JwtAuthenticationFilter (Bearer Token Parsing & Validation)    |
|      - SecurityFilterChain (Role-Based Access Control / RBAC)         |
+-----------------------------------------------------------------------+
                                    |
                                    v
+-----------------------------------------------------------------------+
|                    Controller Layer (@RestController)                 |
|      - AuthController, DisasterController, ShelterController          |
|      - ResourceRequestController, IncidentLogController, UserController|
|      - Bean Validation (@Valid, DTO mapping)                          |
+-----------------------------------------------------------------------+
                                    |
                                    v
+-----------------------------------------------------------------------+
|                    Service Layer (@Service, @Transactional)           |
|      - Business Interfaces & Impl Classes                             |
|      - Custom Exception Propagation (GlobalExceptionHandler)          |
+-----------------------------------------------------------------------+
                                    |
                                    v
+-----------------------------------------------------------------------+
|                    Repository Layer (Spring Data JPA)                 |
|      - JpaRepository Interfaces, Derived & Custom Queries             |
+-----------------------------------------------------------------------+
                                    |
                                    v
+-----------------------------------------------------------------------+
|                    Persistence Engine & Database                      |
|      - Hibernate ORM 6                                                |
|      - MySQL 8.0+ / H2 Embedded DB (Test/Dev)                         |
+-----------------------------------------------------------------------+
```

## Layer Responsibilities

1. **Security Layer**: Intercepts HTTP requests, validates JWT signatures, extracts user credentials & authorities, and enforces Role-Based Access Control (`CITIZEN`, `VOLUNTEER`, `RESCUE_TEAM`, `ADMIN`).
2. **Controller Layer**: Exposes REST endpoints, validates request payloads (`@Valid`), converts DTOs, and returns HTTP response wrappers (`ResponseEntity`).
3. **Service Layer**: Implements core business logic, handles transactional boundaries (`@Transactional`), coordinates repositories, and throws domain exceptions.
4. **Repository Layer**: Provides abstraction over database queries via Spring Data JPA interface contracts.
5. **Database Layer**: Standardized SQL schema normalized to 3NF with index optimization and referential constraint checks.
