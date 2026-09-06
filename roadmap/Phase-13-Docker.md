# Phase-13: Dockerization & Containerized Deployment

## Objectives
- Understand Docker architecture: images, containers, layers, registries, and volumes.
- Write production-grade multi-stage `Dockerfile` for the Spring Boot API.
- Compose multi-container environments with `docker-compose.yml` (Spring Boot + MySQL 8.0).
- Manage containerized service configuration using environment variables and secrets.

## Topics
1. **Docker Core Concepts**:
   - **Image**: Read-only layered template built from a `Dockerfile`.
   - **Container**: Running instance of an image with isolated process, filesystem, and network.
   - **Layer Caching**: Docker caches unchanged `RUN`, `COPY` steps — placing dependency downloads before source code copy maximizes build speed.
   - **Registry**: Docker Hub or private ECR registry storing published images.
2. **Multi-Stage Dockerfile**:
   - Stage 1 (`builder`): JDK 21 image downloads dependencies and runs `mvn package`.
   - Stage 2 (`runtime`): Minimal JRE 21 Alpine image copies only the FAT JAR from builder.
   - Benefits: Reduces final image size from ~500MB (JDK) to ~80MB (JRE-only Alpine).
3. **Docker Compose**:
   - `version`: Compose file format version.
   - `services`: Named service definitions (`db`, `backend`, `frontend`).
   - `depends_on` with `condition: service_healthy`: Prevents API startup before MySQL is ready.
   - `healthcheck`: Docker probes container health on a polling interval.
   - `volumes`: Persists MySQL data across container restarts.
   - `networks`: Isolated bridge network for inter-container communication by service name.
4. **Environment Variable Management**:
   - `.env` file at project root stores sensitive DB credentials and JWT secret without committing to Git.
   - Docker Compose reads `.env` automatically via `${VARIABLE_NAME}` interpolation.

## Mini Project
Create multi-stage `docker/Dockerfile.backend` and `docker/docker-compose.yml` orchestrating MySQL 8.0 + Spring Boot API services with health checks, volume persistence, and environment variable injection.

## Integration into Disaster Management System
Location: `docker/`
- `docker/Dockerfile.backend`: Multi-stage build — Eclipse Temurin JDK 21 builder + JRE 21 Alpine runtime with non-root user security.
- `docker/docker-compose.yml`: MySQL 8.0 + Spring Boot backend with database health check dependency, persistent volume, and bridge network.

## Git Commit
`feat(docker): add multi-stage Dockerfile.backend and docker-compose.yml for containerized deployment`

## Interview Questions
1. *What is a multi-stage Dockerfile and what problem does it solve?*
   - Multi-stage builds use multiple `FROM` statements. Early stages use heavy build tools (JDK, Maven) to compile artifacts. The final stage copies only the compiled artifact into a lightweight runtime image (JRE-only), reducing the production image footprint from ~500MB to ~80MB.
2. *How does `depends_on` with `condition: service_healthy` improve container orchestration?*
   - Without it, Docker Compose starts services by dependency order but does not wait for the database to actually accept connections. `service_healthy` blocks dependent services until the health check passes, preventing database connection failures during Spring Boot startup.

## Completion Checklist
- [x] Written multi-stage `Dockerfile.backend` with Eclipse Temurin JDK 21 + JRE 21 Alpine
- [x] Configured non-root container user for production security
- [x] Composed `docker-compose.yml` with MySQL 8.0 and Spring Boot API services
- [x] Added MySQL health check and `depends_on: service_healthy` gating
- [x] Configured persistent `mysql_data` volume and isolated `disaster-network` bridge
