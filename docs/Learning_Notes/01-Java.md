# Learning Notes: 01 - Java 21 LTS Core Features & Domain Processing

## Overview
Java 21 LTS introduces powerful updates that simplify concurrent programming, functional processing, and domain modeling. In the **Disaster Management System**, Java 21 features are utilized to ensure memory efficiency, type safety, and clear operational domain logic.

---

## 1. Key Java 21 LTS Features

### 1.1 Java Records (Immutable DTOs)
Records eliminate boilerplate code for data transfer objects (DTOs) by automatically generating constructors, getters, `equals()`, `hashCode()`, and `toString()`.

```java
public record DisasterSummaryDto(
    Long id,
    String title,
    String location,
    String severity,
    LocalDateTime reportedAt
) {}
```

### 1.2 Pattern Matching for Switch
Simplifies type inspection and conditional branching when handling polymorphic disaster event payloads or exception mapping.

```java
public String categorizeSeverity(Object severityInput) {
    return switch (severityInput) {
        case Integer level when level >= 4 -> "CRITICAL";
        case Integer level when level == 3 -> "HIGH";
        case Integer level -> "MEDIUM";
        case String status when status.equalsIgnoreCase("URGENT") -> "CRITICAL";
        case null -> "UNKNOWN";
        default -> "LOW";
    };
}
```

### 1.3 Sealed Classes & Interfaces
Restricts which subclasses or implementations can extend a domain model or status event, enforcing domain boundary integrity.

```java
public sealed interface DisasterEvent permits FloodEvent, EarthquakeEvent, FireEvent {
    String getAffectedRegion();
}
```

---

## 2. Collections & Streams API in Disaster Operations

### 2.1 Filtering & Aggregating Shelter Occupancy
Using Java Streams to calculate total available beds across safe emergency shelters in a target city:

```java
public int calculateAvailableBeds(List<Shelter> shelters, String city) {
    return shelters.stream()
        .filter(shelter -> shelter.getCity().equalsIgnoreCase(city))
        .mapToInt(shelter -> shelter.getCapacity() - shelter.getOccupied())
        .filter(available -> available > 0)
        .sum();
}
```

### 2.2 Grouping Resource Requests by Status
Partitioning active requests into operational queues using `Collectors.groupingBy`:

```java
public Map<String, List<ResourceRequest>> groupRequestsByStatus(List<ResourceRequest> requests) {
    return requests.stream()
        .collect(Collectors.groupingBy(
            request -> request.getStatus().name()
        ));
}
```

---

## 3. Exception Handling & Defensive Design

- **Runtime Exception Hierarchy**: Business failures throw specific runtime exceptions (`ResourceNotFoundException`, `UnauthorizedException`, `ResourceConflictException`) rather than generic `Exception` types.
- **Fail-Fast Validation**: Incoming parameters are checked immediately before executing transactional service logic.

---

## 4. Key Takeaways
- Records reduce boilerplate DTO code while guaranteeing immutability.
- Streams pipeline operations provide high-performance, readable crisis data aggregation.
- Strong typing and pattern matching reduce defensive `instanceof` checks.
