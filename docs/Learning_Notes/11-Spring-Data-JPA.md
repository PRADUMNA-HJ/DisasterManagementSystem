# Learning Notes: 11 - Spring Data JPA Repositories & Query Abstraction

## Overview
Spring Data JPA reduces relational data access code by providing high-level repository interfaces (`JpaRepository<T, ID>`) that generate implementation proxies dynamically at application startup.

---

## 1. Spring Data Repository Interface Hierarchy

```text
Repository<T, ID>
    └── CrudRepository<T, ID>
            └── PagingAndSortingRepository<T, ID>
                    └── JpaRepository<T, ID>
```

```java
@Repository
public interface DisasterReportRepository extends JpaRepository<DisasterReport, Long> {
    
    // Derived query method generated automatically by Spring Data JPA
    Page<DisasterReport> findBySeverity(SeverityLevel severity, Pageable pageable);
    
    Page<DisasterReport> findByLocationContainingIgnoreCase(String location, Pageable pageable);
}
```

---

## 2. Derived Query Method Keywords

Spring Data parses method names to generate JPQL queries:

| Keyword | Example | Generated JPQL Equivalent |
| :--- | :--- | :--- |
| `findBy...` | `findByEmail(String email)` | `SELECT u FROM User u WHERE u.email = ?1` |
| `Containing` | `findByCityContaining(String city)` | `SELECT s FROM Shelter s WHERE s.city LIKE %?1%` |
| `IgnoreCase` | `findByCityIgnoreCase(String city)` | `SELECT s FROM Shelter s WHERE UPPER(s.city) = UPPER(?1)` |
| `GreaterThan` | `findByCapacityGreaterThan(int min)` | `SELECT s FROM Shelter s WHERE s.capacity > ?1` |
| `existsBy...` | `existsByEmail(String email)` | `SELECT COUNT(u) > 0 FROM User u WHERE u.email = ?1` |

---

## 3. Custom JPQL & Native Queries (`@Query`)

```java
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    @Query("SELECT s FROM Shelter s WHERE s.city = :city AND (s.capacity - s.occupied) >= :minBeds")
    List<Shelter> findAvailableShelters(@Param("city") String city, @Param("minBeds") int minBeds);
}
```

---

## 4. Key Takeaways
- `JpaRepository` provides built-in CRUD (`save`, `findById`, `findAll`, `deleteById`) out of the box.
- Derived queries turn method signatures directly into safe, optimized JPQL.
- `@Transactional` ensures atomicity and rollback safety across database write operations.
