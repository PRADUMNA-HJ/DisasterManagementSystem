# Learning Notes: 15 - Microservices Architecture & Design Patterns

## Overview
Microservices is an architectural style structuring an application as a collection of small, independently deployable services that each own a single bounded business domain.

---

## 1. Monolith vs Microservices

| Attribute | Disaster API (Monolith) | Future Microservices |
| :--- | :--- | :--- |
| **Deployment unit** | Single `disaster-api.jar` | Multiple independently deployed JARs |
| **Scaling** | Full application scaled together | Individual services scaled independently |
| **Database** | Shared MySQL schema | One database schema per service |
| **Communication** | In-process method calls | HTTP REST / Kafka events |
| **Failure isolation** | One bug can affect all features | Faults contained within service boundary |
| **Team ownership** | All teams on same codebase | Each team owns their service |

---

## 2. Proposed Service Decomposition

```text
                  +--------------------+
  React Frontend  |    API Gateway     |   (Spring Cloud Gateway - Port 8080)
  (Port 3000) --> | /auth/**           |
                  | /disasters/**      |
                  | /shelters/**       |
                  | /resource-req/**   |
                  +----+--+---+----+---+
                       |  |   |    |
         +-------------+  |   |    +--------------+
         |                |   |                   |
         v                v   v                   v
  +------------+  +----------+  +-----------+  +--------------+
  | auth-svc   |  |disaster  |  |shelter    |  |resource-svc  |
  | :8081      |  |  -svc    |  |  -svc     |  |  :8084       |
  |            |  |  :8082   |  |  :8083    |  |              |
  +------+-----+  +-----+----+  +------+----+  +------+-------+
         |               |              |              |
         v               v              v              v
    [auth_db]    [disaster_db]  [shelter_db]   [resource_db]
```

---

## 3. Key Patterns

### Circuit Breaker (Resilience4j)
```java
@CircuitBreaker(name = "disasterService", fallbackMethod = "getDefaultDisasters")
public List<DisasterDto> getDisasters() {
    return disasterServiceClient.getAll();
}

public List<DisasterDto> getDefaultDisasters(Exception e) {
    return Collections.emptyList(); // Graceful fallback
}
```

---

## 4. Key Takeaways
- Start as a well-structured monolith; extract to microservices only when team/scaling needs justify the complexity.
- Each service must be independently deployable — no shared database tables across service boundaries.
- Async messaging (Kafka) decouples services and prevents cascading failures from synchronous dependency chains.
