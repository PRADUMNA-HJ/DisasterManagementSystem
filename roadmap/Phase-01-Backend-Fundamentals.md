# Phase-01: Backend & Java 21 Fundamentals

## Objectives
- Master core Java 21 LTS language features, syntax, and object-oriented programming paradigms.
- Design domain models using OOP principles (Encapsulation, Polymorphism, Inheritance, Abstraction).
- Implement robust exception handling strategies using custom runtime exception hierarchies.
- Leverage Java Collections and Streams API for efficient crisis data aggregation and processing.

## Topics
1. **Java 21 Core Syntax & Modern Features**: Sealed classes, Java Records for immutable DTOs, Pattern Matching for `switch`, Text Blocks, and Virtual Threads.
2. **Object-Oriented Programming (OOP)**:
   - *Encapsulation*: Private domain state accessed via controlled getters/setters/methods.
   - *Polymorphism*: Interface-driven design (`UserService`, `DisasterReportService`).
   - *Inheritance*: Base domain types and custom exception subclasses (`RuntimeException` -> `ResourceNotFoundException`).
   - *Abstraction*: Hiding complex business workflows behind high-level service contracts.
3. **Java Collections Framework**: `List`, `Set`, `Map`, `Queue` implementations (`ArrayList`, `HashSet`, `HashMap`, `ConcurrentHashMap`) for in-memory caching and processing.
4. **Streams API & Lambdas**: Declarative filtering, mapping, sorting, grouping, and reducing disaster statistics.
5. **Exception Handling**: Checked vs Unchecked exceptions, try-with-resources, custom runtime exceptions (`ResourceNotFoundException`, `UnauthorizedException`, `ResourceConflictException`).

## Mini Project
Develop an in-memory Java domain module modeling Disaster Incidents, Emergency Shelters, and Relief Resource Requests with full filtering and validation.

## Integration into Disaster Management System
Applied in `backend/disaster-api`:
- Abstracted operations using interfaces: `DisasterReportService`, `ShelterService`, `ResourceRequestService`, `UserService`, `AuthService`.
- Model entities using Java domain state with encapsulation and Lombok annotations (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`).
- Custom Runtime Exceptions in `com.disastermanagement.exception`: `ResourceNotFoundException`, `ResourceConflictException`, `UnauthorizedException`, `InvalidOperationException`.
- Stream operations applied across services for DTO mapping and data transformation.

## Git Commit
`feat(core): implement core domain interfaces, exception hierarchy, and model abstractions`

## Interview Questions
1. *What is the difference between Checked and Unchecked Exceptions in Java?*
   - Checked exceptions (`Exception`) must be explicitly declared in method signatures (`throws`) or caught at compile-time. Unchecked exceptions (`RuntimeException`) represent unchecked programmatic errors and business rule violations.
2. *How do Java Streams improve performance and readability when handling collections?*
   - Streams enable functional pipeline operations (`filter`, `map`, `collect`), lazy evaluation, and simple parallel processing without boilerplate loops.

## Completion Checklist
- [x] Defined core domain domain entities (`User`, `DisasterReport`, `Shelter`, `ResourceRequest`, `IncidentLog`)
- [x] Built domain service interfaces adhering to Abstraction and Interface Segregation
- [x] Established custom runtime exception hierarchy for global exception handling
- [x] Utilized Java Streams for data mapping and payload validation
