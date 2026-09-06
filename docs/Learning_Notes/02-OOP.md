# Learning Notes: 02 - Object-Oriented Programming (OOP) & SOLID Principles

## Overview
Object-Oriented Programming (OOP) provides the structural foundation for the **Disaster Management System**. Applying OOP principles ensures clear boundaries between business entities, service contracts, data repositories, and HTTP controllers.

---

## 1. Core OOP Pillars in Disaster Management

### 1.1 Encapsulation
Domain state inside JPA entities (e.g., `User`, `Shelter`, `DisasterReport`) is kept private. Access and state mutations are controlled via explicit getters, setters, and builder methods.

```java
@Entity
@Table(name = "shelters")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false)
    private Integer occupied;
    
    // Controlled business method preventing invalid state mutation
    public boolean canAccommodate(int additionalOccupants) {
        return (this.occupied + additionalOccupants) <= this.capacity;
    }
}
```

### 1.2 Polymorphism
Polymorphism allows controller layers to depend on service interfaces (`DisasterReportService`, `ShelterService`) rather than concrete implementations. This enables seamless mock substitution in unit tests.

```java
// Controller depends on interface contract, not concrete implementation
private final DisasterReportService disasterReportService;
```

### 1.3 Abstraction
Hiding complex data mapping, transaction handling, and repository queries behind clean, high-level interface method signatures (`getDisasterById`, `registerShelter`).

### 1.4 Inheritance
Shared entity metadata (such as timestamps) and custom exception classes inherit base functionality from parent Java classes:

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

## 2. SOLID Principles Applied

| Principle | Description | Disaster Management Implementation |
| :--- | :--- | :--- |
| **S** - Single Responsibility | A class should have only one reason to change. | `AuthController` handles HTTP requests; `AuthService` handles authentication logic; `UserRepository` handles queries. |
| **O** - Open/Closed | Software entities should be open for extension, closed for modification. | Exception handling extended via `@RestControllerAdvice` without modifying service classes. |
| **L** - Liskov Substitution | Subtypes must be substitutable for their base types. | Mock implementation of `ShelterService` in unit tests can replace `ShelterServiceImpl`. |
| **I** - Interface Segregation | Clients shouldn't be forced to depend on methods they don't use. | Focused interfaces (`ShelterService`, `ResourceRequestService`) rather than a monolithic `SystemManager`. |
| **D** - Dependency Inversion | Depend on abstractions, not concretions. | High-level Controllers depend on Service interfaces; Services depend on Spring Data JPA Repository interfaces. |

---

## 3. Key Takeaways
- Encapsulation prevents invalid state transitions (e.g., negative shelter capacity).
- Interface-driven design enforces loose coupling and simplifies test automation.
- SOLID principles reduce code rigidity and simplify future microservices decomposition.
