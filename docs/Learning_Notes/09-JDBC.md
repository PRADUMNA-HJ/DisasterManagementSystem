# Learning Notes: 09 - JDBC Architecture & Connection Pooling

## Overview
Java Database Connectivity (JDBC) is the low-level Java API for executing SQL statements against relational databases. Spring Data JPA and Hibernate build on top of JDBC to provide object-relational mapping and repository abstractions.

---

## 1. Core JDBC Interfaces

```text
DriverManager ---> Connection ---> PreparedStatement ---> ResultSet
```

- **`DriverManager`**: Loads DB drivers (`com.mysql.cj.jdbc.Driver`).
- **`Connection`**: Manages session state and transaction boundaries (`commit()`, `rollback()`).
- **`PreparedStatement`**: Precompiled SQL query wrapper with bind parameter placeholders (`?`).
- **`ResultSet`**: Iterative data cursor over query output rows (`rs.next()`, `rs.getString()`).

---

## 2. PreparedStatement vs Raw Statement

```java
// SECURE: Parameterized PreparedStatement prevents SQL Injection
String sql = "SELECT * FROM users WHERE email = ?";
try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setString(1, userEmail);
    try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            String name = rs.getString("full_name");
        }
    }
}
```

---

## 3. Connection Pooling (HikariCP)

Opening raw database connections introduces high TCP and authentication latency (~50-100ms per request). **HikariCP** maintains a warm pool of active connections for immediate reuse:

```properties
# HikariCP Connection Pool Configuration in Spring Boot
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=20000
```

---

## 4. Key Takeaways
- `PreparedStatement` parameter binding is critical for preventing SQL Injection attacks.
- Connection pooling eliminates physical connection establishment overhead on every HTTP request.
- Spring Data JPA abstracts away imperative JDBC boilerplate while preserving low-level performance.
