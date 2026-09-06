# Phase-14: Microservices Architecture & Service Decomposition

## Objectives
- Understand Microservices architectural style and contrast with Modular Monolith (current project state).
- Identify service decomposition boundaries based on bounded contexts and domain ownership.
- Learn core microservices patterns: API Gateway, Service Discovery, Inter-Service Communication, and Circuit Breaker.
- Plan a phased migration strategy from the Disaster Management System monolith to microservices.

## Topics
1. **Microservices vs Monolith**:
   - **Monolith**: Single deployable unit with all features bundled (`disaster-api` JAR). Simple but limited scalability of individual domains.
   - **Microservices**: Independently deployable services owning single bounded contexts with their own databases.
   - Tradeoffs: Distributed complexity, network latency, eventual consistency vs independent scaling and deployment.
2. **Service Decomposition by Domain**:
   - **Auth Service**: User registration, login, JWT issuance (`/auth/**`).
   - **Disaster Service**: Disaster incident CRUD, search, severity filtering (`/disasters/**`).
   - **Shelter Service**: Emergency shelter management and capacity tracking (`/shelters/**`).
   - **Resource Service**: Relief request submission and fulfillment (`/resource-requests/**`).
   - **Notification Service**: Alert push and SMS dispatch (future).
3. **Key Microservices Patterns**:
   - **API Gateway** (Spring Cloud Gateway): Single entry point routing requests to downstream services.
   - **Service Discovery** (Netflix Eureka): Dynamic service registration and DNS-based lookup.
   - **Inter-Service Communication**: REST (sync) via `WebClient`/`FeignClient` or Messaging (async) via Kafka/RabbitMQ.
   - **Circuit Breaker** (Resilience4j): Fail-fast pattern preventing cascading failure across services.
4. **Database per Service Pattern**:
   - Each microservice owns its own isolated database schema.
   - Cross-service joins replaced by API composition or event streaming.

## Mini Project
Design the Disaster Management System microservice architecture with service boundaries, API gateway routing configuration, and Eureka service discovery registry setup.

## Integration into Disaster Management System
Location: `docs/Architecture/FutureMicroserviceMigration.md`
- Phase 1: Extract Auth and User domain into `auth-service`.
- Phase 2: Extract Disaster Report domain into `disaster-service`.
- Phase 3: Extract Shelter and Resource Request domains.
- Phase 4: Introduce Kafka event bus for cross-service notifications.

## Git Commit
`docs(microservices): document microservice decomposition plan, API gateway design, and migration roadmap`

## Interview Questions
1. *What is the API Gateway pattern and why is it critical for microservices?*
   - The API Gateway is a single ingress point for all client requests. It handles cross-cutting concerns (authentication, rate limiting, logging, SSL termination) and routes requests to appropriate upstream microservices, preventing direct service-to-service exposure.
2. *How does the Circuit Breaker pattern prevent cascading failures in microservices?*
   - A Circuit Breaker monitors downstream service error rates. When failures exceed a threshold, the circuit "opens" and immediately returns a fallback response instead of waiting for timeout — preventing resource exhaustion and cascading latency spikes across the service mesh.

## Completion Checklist
- [x] Documented monolith-to-microservices decomposition strategy by bounded context
- [x] Designed API Gateway routing and service registry topology
- [x] Documented inter-service communication patterns (REST/FeignClient + async Kafka)
- [x] Planned database-per-service isolation migration
