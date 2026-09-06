# Workspace Folder Structure

```text
DisasterManagementSystem/
├── .github/                             # GitHub Actions CI/CD workflows
├── backend/
│   └── disaster-api/                    # Main Spring Boot backend project
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/disastermanagement/
│       │   │   │   ├── config/          # Spring Security, CORS, OpenAPI Beans
│       │   │   │   ├── controller/      # REST API Controllers (@RestController)
│       │   │   │   ├── dto/             # Request & Response Data Transfer Objects
│       │   │   │   ├── entity/          # JPA Persistence Entities
│       │   │   │   ├── enums/           # System Enums (RoleType, ResourceType, etc.)
│       │   │   │   ├── exception/       # GlobalExceptionHandler & Custom Exceptions
│       │   │   │   ├── repository/      # Spring Data JPA Interfaces
│       │   │   │   ├── security/        # JwtService, JwtAuthenticationFilter
│       │   │   │   └── service/         # Service Interfaces & Implementations
│       │   │   └── resources/
│       │   │       ├── application.properties
│       │   │       ├── schema.sql
│       │   │       └── data.sql
│       │   └── test/                    # Unit, Repository, Service & Controller Tests
│       └── pom.xml                      # Maven build specification
├── database/                            # Database DDL/DML scripts & ER docs
│   ├── schema.sql                       # 3NF Table DDLs & Foreign Key Constraints
│   ├── data.sql                         # Initial Seed Data for Roles/Users/Shelters
│   ├── queries.sql                      # Operational SQL Queries & Aggregations
│   ├── relationships.md                 # Table Relationships & Cardinality documentation
│   └── database_design.md               # Full Database Architectural Specification
├── docs/                                # Technical Architecture & Learning Documentation
│   ├── API_Documentation/               # Swagger & OpenAPI guide
│   ├── Architecture/                    # Architectural blueprints & standards
│   ├── Database/                        # Database modeling guides
│   ├── Learning_Notes/                  # 16-Phase learning notes (01-Java to 16-AWS)
│   └── Workflow/                        # Incident lifecycle & operational workflows
├── docker/                              # Container manifests (Dockerfile & Compose)
├── postman/                             # Postman Collection & Environment JSONs
├── roadmap/                             # 16-Phase milestone tracking documents
└── README.md                            # Comprehensive system documentation
```
