# Phase-10: Spring Data JPA Repositories & Data Abstraction

## Objectives
- Master Spring Data JPA repository abstraction (`JpaRepository<T, ID>`, `CrudRepository`, `PagingAndSortingRepository`).
- Write derived query methods for expressive database operations without writing SQL.
- Write custom Jakarta Persistence Query Language (JPQL) and native SQL queries using `@Query`.
- Implement pagination, sorting (`Pageable`, `Page`), and declarative transaction boundaries (`@Transactional`).

## Topics
1. **Repository Hierarchy**:
   - `Repository` -> `CrudRepository` -> `PagingAndSortingRepository` -> `JpaRepository`.
   - Automatic runtime proxy bean generation by Spring Data JPA.
2. **Derived Query Methods**:
   - Method naming parsing rules: `findByEmail`, `findByCityContainingIgnoreCase`, `findBySeverityAndReportedAtAfter`.
   - Operators: `Equals`, `Like`, `Containing`, `IgnoreCase`, `Between`, `LessThan`, `GreaterThan`, `In`, `IsTrue`.
3. **Custom Queries with `@Query`**:
   - JPQL queries operating on entity model names (`SELECT u FROM User u WHERE u.email = :email`).
   - Native SQL queries (`nativeQuery = true`).
   - `@Modifying` annotations for bulk `UPDATE` and `DELETE` queries.
4. **Pagination & Sorting**:
   - Passing `Pageable` parameters to repository methods (`PageRequest.of(page, size, Sort.by(dir, field))`).
   - Returning `Page<T>` containing content list and total elements count.
5. **Transaction Management (`@Transactional`)**:
   - Declarative transaction boundaries on service methods.
   - Read-only transaction optimizations (`@Transactional(readOnly = true)`).

## Mini Project
Create Spring Data JPA repository interfaces for `User`, `Role`, `Shelter`, `DisasterReport`, `ResourceRequest`, and `IncidentLog` with custom search and derived query methods.

## Integration into Disaster Management System
Location: `backend/disaster-api/src/main/java/com/disastermanagement/repository/`
- `UserRepository`: `findByEmail(String email)`, `existsByEmail(String email)`.
- `RoleRepository`: `findByName(RoleType name)`.
- `ShelterRepository`: `findByCityContainingIgnoreCase(String city, Pageable pageable)`.
- `DisasterReportRepository`: `findBySeverity(SeverityLevel severity, Pageable pageable)`, `findByLocationContainingIgnoreCase(String location, Pageable pageable)`.
- `ResourceRequestRepository`: `findByStatus(RequestStatus status, Pageable pageable)`, `findByUserId(Long userId, Pageable pageable)`.
- `IncidentLogRepository`: `findByDisasterReportIdOrderByCreatedAtAsc(Long disasterReportId)`.

## Git Commit
`feat(jpa): implement Spring Data JPA repository interfaces, derived queries, and pagination`

## Interview Questions
1. *How do Derived Query Methods work in Spring Data JPA?*
   - Spring Data JPA parses the method name according to subject and predicate conventions (e.g., `findByCityAndCapacityGreaterThan`), builds an Abstract Syntax Tree (AST), and generates the underlying JPQL/SQL query dynamically at startup.
2. *What is the purpose of `@Transactional(readOnly = true)`?*
   - Signals to Hibernate and the database driver that no dirty checking or entity modifications will occur, allowing Hibernate to disable flush checks and memory tracking, improving read performance.

## Completion Checklist
- [x] Created `JpaRepository` interfaces across all 6 domain entities
- [x] Defined derived query methods for filtering and validation checks
- [x] Configured pagination with `Pageable` and `Page<T>` return types
- [x] Applied `@Transactional` boundaries across service implementations
