# Disaster Management System

## Project Overview

A comprehensive Disaster Management System designed to facilitate coordination and response during emergency situations. The system empowers citizens to report disasters, allows volunteers to register, enables rescue teams to manage field operations, and provides government officials and administrators with tools to monitor incidents and manage the system effectively.

## Objectives

- Provide a structured approach to disaster reporting and management.
- Ensure seamless communication between citizens, volunteers, and rescue teams.
- Create an enterprise-grade backend architecture following Layered MVC patterns.
- Secure all operations using stateless JWT authentication with Role-Based Access Control (RBAC).
- Provide OpenAPI/Swagger interactive documentation and Postman collections.
- Prepare the foundation for a future migration to a Microservices architecture.

## Technology Stack

- **Backend**: Java 21, Spring Boot 3.3.1, Spring Security 6, JWT (JJWT 0.12.6), Spring Data JPA, Hibernate, MySQL, H2 In-Memory DB, Bean Validation, Lombok
- **API Documentation**: Springdoc OpenAPI / Swagger UI (OpenAPI 3.0)
- **Frontend**: React (Separate Application — Planned)
- **Database**: MySQL 8.0+ / H2 In-Memory (Test/Dev)
- **Testing**: JUnit 5, Mockito, Spring Boot Test, MockMvc, AssertJ
- **DevOps**: Docker, Maven, Git

## High-Level Architecture

The project follows a standard Layered MVC Architecture for clear separation of concerns, scalability, and maintainability:

```text
HTTP Request (React / Axios / Postman)
    ↓
JwtAuthenticationFilter (Bearer Token Validation)
    ↓
SecurityFilterChain (Role-Based Access Control)
    ↓
Controller Layer (@RestController, DTO Validation)
    ↓
Service Layer (Interfaces & Business Logic, @Transactional)
    ↓
Repository Layer (Spring Data JPA, Pagination & Derived Queries)
    ↓
Hibernate ORM 6
    ↓
MySQL 8.0+ / H2 Database
```

## Folder Structure

```text
DisasterManagementSystem/
├── backend/
│   └── disaster-api/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/disastermanagement/
│       │   │   │   ├── config/          # SecurityConfig, CorsConfig, OpenApiConfig
│       │   │   │   ├── controller/      # AuthController, DisasterController, ShelterController, etc.
│       │   │   │   ├── dto/             # Request & Response DTOs, PagedResponse, Auth DTOs
│       │   │   │   ├── entity/          # JPA Entities (User, Role, DisasterReport, Shelter, etc.)
│       │   │   │   ├── enums/           # RoleType, RequestStatus, ResourceType
│       │   │   │   ├── exception/       # GlobalExceptionHandler, ResourceNotFoundException, etc.
│       │   │   │   ├── repository/      # Spring Data JPA Repositories
│       │   │   │   ├── security/        # JwtService, JwtAuthenticationFilter, CustomUserDetailsService
│       │   │   │   └── service/         # Service interfaces and implementations
│       │   │   └── resources/
│       │   │       ├── application.properties
│       │   │       ├── schema.sql
│       │   │       └── data.sql
│       │   └── test/                    # 39 Unit, Repository, Controller & Integration Tests
│       └── pom.xml
├── database/                            # DDL schemas, DML seed data, ER documentation
├── docs/                                # Architectural guides, API specs, learning notes
├── postman/                             # Postman collection & environment JSON files
├── roadmap/                             # Step-by-step learning & development roadmap
└── README.md
```

## Development Roadmap Status

| Phase | Milestone | Status |
|:------|:----------|:------:|
| **Phase 1** | Initial Project Setup and Architecture Validation | ✅ COMPLETED |
| **Phase 2** | Database Schema Design and Entity Modeling (6 Tables, 3NF) | ✅ COMPLETED |
| **Phase 3** | Core REST API Implementation (Full CRUD, Global Exception Handler, Pagination) | ✅ COMPLETED |
| **Phase 4** | Spring Security & JWT Integration (Stateless Auth, RBAC, BCrypt) | ✅ COMPLETED |
| **Phase 5** | Frontend Integration (React Application) | ⏳ NEXT PHASE |
| **Phase 6** | Containerization (Docker) and CI/CD Setup | ⏳ PLANNED |

## Running the Application Locally

### Prerequisites
- **Java**: JDK 21+
- **Maven**: 3.8+ (or Maven Wrapper)
- **MySQL**: 8.0+ (Optional — unit/integration tests run on embedded H2)

### 1. Build and Run Tests
```bash
cd backend/disaster-api
mvn clean test
```

### 2. Run the Spring Boot Application
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

### 3. Interactive API Documentation (Swagger UI)
Once started, open your browser to explore and test all APIs interactively:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### 4. Postman Collection
Import the following files into Postman:
- Collection: `postman/Disaster_Management_System.postman_collection.json`
- Environment: `postman/Disaster_Management_Local.postman_environment.json`

## API Endpoints Summary

### Authentication (`/api/v1/auth`)
| Method | Endpoint | Access | Description |
|:-------|:---------|:------:|:------------|
| `POST` | `/api/v1/auth/register` | Public | Register new user account (CITIZEN, VOLUNTEER, RESCUE_TEAM) |
| `POST` | `/api/v1/auth/login` | Public | Authenticate with email/password and receive JWT |
| `GET` | `/api/v1/auth/me` | Authenticated | Get current authenticated user profile |

### Disaster Reports (`/api/v1/disasters`)
| Method | Endpoint | Access | Description |
|:-------|:---------|:------:|:------------|
| `GET` | `/api/v1/disasters` | Public | Retrieve paginated disaster reports (`page`, `size`, `sort`) |
| `GET` | `/api/v1/disasters/{id}` | Public | Retrieve single disaster by ID |
| `GET` | `/api/v1/disasters/search` | Public | Search disasters by location |
| `GET` | `/api/v1/disasters/filter` | Public | Filter disasters by severity level |
| `POST` | `/api/v1/disasters` | Authenticated | Report a new disaster incident |
| `PUT` | `/api/v1/disasters/{id}` | ADMIN, RESCUE_TEAM | Update existing disaster report |
| `DELETE` | `/api/v1/disasters/{id}` | ADMIN | Delete disaster record |

### Emergency Shelters (`/api/v1/shelters`)
| Method | Endpoint | Access | Description |
|:-------|:---------|:------:|:------------|
| `GET` | `/api/v1/shelters` | Public | Retrieve paginated list of shelters |
| `GET` | `/api/v1/shelters/{id}` | Public | Retrieve shelter by ID |
| `GET` | `/api/v1/shelters/search` | Public | Search shelters by city |
| `POST` | `/api/v1/shelters` | ADMIN | Register a new shelter |
| `PUT` | `/api/v1/shelters/{id}` | ADMIN | Update shelter information |
| `DELETE` | `/api/v1/shelters/{id}` | ADMIN | Delete shelter record |

### Resource Requests (`/api/v1/resource-requests`)
| Method | Endpoint | Access | Description |
|:-------|:---------|:------:|:------------|
| `GET` | `/api/v1/resource-requests` | Authenticated | Retrieve paginated resource requests |
| `GET` | `/api/v1/resource-requests/{id}` | Authenticated | Retrieve single resource request by ID |
| `GET` | `/api/v1/resource-requests/status` | Authenticated | Filter requests by status (PENDING, APPROVED, DELIVERED) |
| `GET` | `/api/v1/resource-requests/user/{userId}` | Authenticated | Retrieve requests submitted by a user |
| `POST` | `/api/v1/resource-requests` | Authenticated | Submit relief resource request |
| `PUT` | `/api/v1/resource-requests/{id}/status` | ADMIN, RESCUE_TEAM, VOLUNTEER | Update request fulfillment status |
| `DELETE` | `/api/v1/resource-requests/{id}` | ADMIN | Delete resource request |

### Incident Logs (`/api/v1/incident-logs`)
| Method | Endpoint | Access | Description |
|:-------|:---------|:------:|:------------|
| `GET` | `/api/v1/incident-logs/disaster/{id}` | Authenticated | Get audit logs for a disaster |
| `POST` | `/api/v1/incident-logs` | ADMIN, RESCUE_TEAM, VOLUNTEER | Record new incident log update |
| `DELETE` | `/api/v1/incident-logs/{id}` | ADMIN | Delete incident log entry |

### User Management (`/api/v1/users` - Admin Only)
| Method | Endpoint | Access | Description |
|:-------|:---------|:------:|:------------|
| `GET` | `/api/v1/users` | ADMIN | Retrieve all registered users |
| `GET` | `/api/v1/users/{id}` | ADMIN | Retrieve user details by ID |
| `POST` | `/api/v1/users` | ADMIN | Create user account |
| `PUT` | `/api/v1/users/{id}` | ADMIN | Update user account |
| `DELETE` | `/api/v1/users/{id}` | ADMIN | Delete user account |

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
