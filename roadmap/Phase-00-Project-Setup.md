# Phase-00: Project Setup & Environment Architecture

## Objectives
- Establish standard workspace layout for a enterprise-grade modular Java application.
- Configure Java 21 LTS development runtime and Maven 3.8+ build automation environment.
- Initialize Git repository control flow, branching guidelines, and clean `.gitignore`.
- Structure modular hierarchy across `backend`, `database`, `docs`, `docker`, `roadmap`, and `postman`.

## Topics
1. **Environment Setup**: JDK 21 installation, `JAVA_HOME` environment configuration, Maven wrapper integration.
2. **Version Control**: Git workflow, `.gitignore` scoping for Java/Maven/IDE build artifacts, commit semantics.
3. **Repository Directory Layout**: Separation of concerns between backend application runtime, database DDL/DML, Postman integration suites, dockerized manifests, and architectural docs.
4. **IDE & Tooling**: VS Code / IntelliJ IDEA / Eclipse Java compiler settings, Lombok annotation processing enablements.

## Mini Project
Initialize git tracking, set up project root directory layout, and craft foundational `.gitignore` and `README.md` introducing the Disaster Management System project scope.

## Integration into Disaster Management System
Created multi-module repository root supporting:
- `/backend/disaster-api`: Spring Boot 3.3.1 application root.
- `/database`: Relational database DDL (`schema.sql`), DML (`data.sql`), operational queries (`queries.sql`), and relationship documentation (`relationships.md`).
- `/docs`: Enterprise architectural blueprints, ER diagrams, REST API specs, sequence diagrams, and learning logs.
- `/docker`: Dockerfiles and multi-container `docker-compose.yml` declarations.
- `/roadmap`: Phase-by-phase learning curriculum tracking.
- `/postman`: Postman collection and local environment configuration files.

## Git Commit
`feat(setup): initialize disaster management system directory architecture and workspace config`

## Interview Questions
1. *Why is Java 21 LTS chosen for enterprise applications?*
   - Long-Term Support (LTS), performance optimizations, virtual threads (Project Loom), pattern matching, records, and enhanced API capabilities.
2. *What is the purpose of `.gitignore` and what build targets should be ignored in Java projects?*
   - Prevents compiled bytecode (`.class`), target build directories (`/target`), IDE configurations (`.idea/`, `.settings/`), environment files (`.env`), and OS secrets from polluting the repository.

## Completion Checklist
- [x] Installed JDK 21 and verified with `java -version`
- [x] Initialized Git repository and created root `.gitignore`
- [x] Defined folder structure for `backend`, `database`, `docs`, `docker`, `roadmap`, and `postman`
- [x] Documented system overview and architecture in root `README.md`
