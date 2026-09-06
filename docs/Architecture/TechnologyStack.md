# Technology Stack & Selection Justifications

| Component | Technology | Version | Justification |
| :--- | :--- | :--- | :--- |
| **Language** | Java | 21 LTS | Long-Term Support, Virtual Threads, Records, Pattern Matching, enhanced performance. |
| **Framework** | Spring Boot | 3.3.1 | Enterprise standard, embedded Tomcat server, starter dependencies, auto-configuration. |
| **Security** | Spring Security | 6.x | Enterprise Security, Stateless authentication filter chain, BCrypt password hashing. |
| **JWT** | JJWT | 0.12.6 | Industry standard library for building, parsing, and validating JSON Web Tokens. |
| **ORM / Data Access** | Spring Data JPA / Hibernate | 6.x | High-level data abstraction, automatic query generation, OR mapping, connection pooling. |
| **RDBMS** | MySQL / H2 | 8.0+ / In-Memory | Relational 3NF transaction support; H2 embedded database for fast local unit/integration tests. |
| **API Docs** | Springdoc OpenAPI | 2.6.0 | Auto-generated interactive Swagger UI and OpenAPI 3.0 specification. |
| **Build Tool** | Apache Maven | 3.8+ | Standardized dependency management, multi-stage lifecycle automation, executable JAR packaging. |
| **Testing** | JUnit 5, Mockito, AssertJ | 5.x | Comprehensive unit, repository, service, and MockMvc controller testing. |
