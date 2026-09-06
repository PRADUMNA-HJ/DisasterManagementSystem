# Phase-08: JDBC Architecture & Persistence Infrastructure

## Objectives
- Understand Java Database Connectivity (JDBC) API architecture and execution flow.
- Master connection management, `PreparedStatement` parameterization, and `ResultSet` mapping.
- Understand connection pooling mechanisms (HikariCP) and performance implications.
- Transition from low-level imperative JDBC to declarative Spring Data JPA / Hibernate ORM abstraction.

## Topics
1. **JDBC Architecture**:
   - `DriverManager`: Registers JDBC drivers (`com.mysql.cj.jdbc.Driver`, `org.h2.Driver`).
   - `Connection`: Manages active database session and transaction boundaries.
   - `Statement` vs `PreparedStatement`: Precompiled SQL preventing SQL Injection vulnerabilities.
   - `ResultSet`: Cursor-based processing of database query results.
2. **Connection Pooling**:
   - Why creating raw database connections per request is expensive.
   - HikariCP connection pool architecture: High performance, lightweight, zero-overhead connection reuse.
   - Pool properties: `maximum-pool-size`, `minimum-idle`, `idle-timeout`, `connection-timeout`.
3. **JDBC Exception & Resource Management**:
   - `SQLException` handling and SQL state codes.
   - Resource cleanup using try-with-resources (`AutoCloseable` interface).

## Mini Project
Analyze low-level JDBC data access patterns vs Spring Boot auto-configured HikariCP DataSource and Spring Data JPA persistence.

## Integration into Disaster Management System
Location: `backend/disaster-api`
- Spring Boot automatically configures HikariCP DataSource (`com.zaxxer.hikari.HikariDataSource`).
- PreparedStatements generated under the hood by Hibernate ORM & Spring Data JPA.
- `application.properties` connection pool parameters (`spring.datasource.hikari.maximum-pool-size=10`).

## Git Commit
`docs(jdbc): document JDBC API architecture, HikariCP connection pooling, and ORM transition`

## Interview Questions
1. *Why should `PreparedStatement` always be used instead of standard `Statement` in JDBC?*
   - `PreparedStatement` pre-compiles the SQL query template on the database server and safely binds user input parameters, preventing SQL Injection attacks and improving execution speed via query plan caching.
2. *What is a Connection Pool and why is HikariCP the default in Spring Boot?*
   - A connection pool maintains a set of reusable physical database connections, eliminating the high latency of establishing a TCP connection per HTTP request. HikariCP is favored for its fast bytecode manipulation and low lock contention.

## Completion Checklist
- [x] Documented JDBC driver registration, Connection, PreparedStatement, and ResultSet lifecycle
- [x] Configured HikariCP connection pooling in Spring Boot application runtime
- [x] Verified SQL injection prevention via parameterized queries
- [x] Abstracted raw JDBC calls into Spring Data JPA repository layer
