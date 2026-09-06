# Learning Notes: 14 - Docker Containerization & Compose Orchestration

## Overview
Docker enables packaging the **Disaster Management System** backend and its dependencies into lightweight, portable containers that run identically across development, staging, and production environments.

---

## 1. Docker Key Concepts

| Concept | Description |
| :--- | :--- |
| **Image** | Immutable, layered template built from `Dockerfile` instructions. |
| **Container** | Running instance of an image with isolated PID, network, and filesystem. |
| **Layer** | Each `RUN`/`COPY`/`ADD` instruction creates a cached filesystem layer. |
| **Volume** | Persistent storage mounted into containers, surviving container restarts. |
| **Network** | Virtual network enabling inter-container DNS resolution by service name. |

---

## 2. Multi-Stage Dockerfile

Multi-stage builds separate compilation from the runtime image:

```dockerfile
# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B     # Cached layer: dependencies
COPY src ./src
RUN ./mvnw clean package -DskipTests    # Compile and package JAR

# Stage 2: Minimal Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
RUN addgroup -S disaster && adduser -S disaster -G disaster
COPY --from=builder /app/target/disaster-api-*.jar app.jar
USER disaster
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Result**: Final image is ~80MB (JRE Alpine) vs ~500MB (full JDK).

---

## 3. Docker Compose — Key Directives

```yaml
services:
  db:
    image: mysql:8.0
    healthcheck:                          # Probe DB readiness
      test: ["CMD", "mysqladmin", "ping"]
      interval: 10s
      retries: 5
    volumes:
      - mysql_data:/var/lib/mysql         # Persist data across restarts

  backend:
    build:
      context: ./backend/disaster-api     # Build context
    depends_on:
      db:
        condition: service_healthy        # Don't start until DB is ready
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/disaster_db  # Resolves by service name
```

---

## 4. Key Takeaways
- Layer ordering matters: copy `pom.xml` and download dependencies before copying source code to maximize cache reuse.
- `service_healthy` prevents Spring Boot startup failures due to MySQL not being ready.
- Inter-container networking by service name (`db`, `backend`) eliminates hardcoded IP addresses.
