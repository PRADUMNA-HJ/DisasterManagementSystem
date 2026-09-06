# Phase-04: Spring Boot Architecture & Auto-Configuration

## Objectives
- Understand Spring Boot framework design, starter dependencies, and auto-configuration mechanisms.
- Master externalized configuration using `application.properties` / `application.yml` and environment profiles.
- Configure embedded Tomcat servlet container and HTTP web runtime options.
- Utilize Spring Boot Actuator for health checks and operational metrics monitoring.

## Topics
1. **Spring Boot Core Concepts**:
   - Opinionated defaults reducing XML/Java boilerplate configuration.
   - Entry point `@SpringBootApplication`: Composed of `@SpringBootConfiguration`, `@EnableAutoConfiguration`, and `@ComponentScan`.
   - Spring Boot Starters (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `spring-boot-starter-security`).
2. **Auto-Configuration Mechanism**:
   - Conditional bean creation via `@ConditionalOnClass`, `@ConditionalOnMissingBean`, `@ConditionalOnProperty`.
   - Inspection of auto-configuration imports.
3. **Externalized Configuration & Profiles**:
   - Hierarchical property resolution (`application.properties`).
   - Profile-specific configuration (`application-dev.properties`, `application-test.properties`, `application-prod.properties`).
   - Injecting values with `@Value("${property.name}")` and `@ConfigurationProperties`.
4. **Embedded Web Server**:
   - Embedded Tomcat (default), Jetty, or Undertow server setup.
   - Server port (`server.port=8080`) and context path configuration.

## Mini Project
Configure `backend/disaster-api` with Spring Boot 3.3.1 auto-configuration, dual database profile setup (in-memory H2 for testing/dev vs MySQL 8.0+ for production), logging configuration, and health endpoints.

## Integration into Disaster Management System
Location: `backend/disaster-api/src/main/resources/application.properties`
- Configured embedded H2 database console and MySQL data source connection properties.
- Configured JPA / Hibernate DDL auto behavior (`spring.jpa.hibernate.ddl-auto=update`).
- Defined JWT secret key and expiration properties (`application.security.jwt.secret-key`, `expiration`).
- Configured Springdoc OpenAPI Swagger UI endpoints.

## Git Commit
`feat(boot): configure Spring Boot 3.3.1 auto-configuration, application.properties, and multi-profile runtime`

## Interview Questions
1. *How does `@EnableAutoConfiguration` work in Spring Boot?*
   - `@EnableAutoConfiguration` scans the classpath for jar dependencies (e.g., `h2`, `spring-web`) and automatically registers pre-configured Spring beans matching conditional rules (`@ConditionalOnClass`).
2. *How do Spring Boot Starters simplify dependency management?*
   - Starters aggregate compatible transitive dependencies and library versions into a single dependency import (e.g., `spring-boot-starter-web` imports Tomcat, Jackson, Spring MVC, and Validation).

## Completion Checklist
- [x] Created `DisasterApiApplication.java` entry point annotated with `@SpringBootApplication`
- [x] Configured `application.properties` for database connections, JPA, logging, and JWT
- [x] Configured embedded H2 database console access for dev/testing
- [x] Verified application startup and auto-configuration on port 8080
