# Phase-07: Relational SQL & Database Design

## Objectives
- Master relational database concepts, ER modeling, and Third Normal Form (3NF) normalization.
- Write Data Definition Language (DDL) schemas with explicit data types and integrity constraints.
- Write Data Manipulation Language (DML) queries for seeding and data operations.
- Construct complex SQL queries using JOINs, aggregate functions (`COUNT`, `SUM`, `AVG`), `GROUP BY`, and `HAVING`.

## Topics
1. **Relational Database Design & 3NF**:
   - Primary Keys (`PRIMARY KEY`), Foreign Keys (`FOREIGN KEY`), Candidate Keys, Surrogate vs Natural Keys.
   - Normal Forms: 1NF (atomic values), 2NF (full functional dependency), 3NF (no transitive dependencies).
2. **Data Definition Language (DDL)**:
   - `CREATE TABLE`, `ALTER TABLE`, `DROP TABLE`.
   - Integrity constraints: `NOT NULL`, `UNIQUE`, `DEFAULT`, `CHECK`, `FOREIGN KEY ON DELETE CASCADE / RESTRICT`.
3. **Data Manipulation Language (DML)**:
   - `INSERT INTO ... VALUES ...`
   - `SELECT ... FROM ... WHERE ... ORDER BY ... LIMIT ... OFFSET ...`
   - `UPDATE ... SET ... WHERE ...`
   - `DELETE FROM ... WHERE ...`
4. **Advanced SQL Operations**:
   - `INNER JOIN`, `LEFT JOIN`, `RIGHT JOIN`, `FULL OUTER JOIN`.
   - Aggregations: `COUNT()`, `SUM()`, `AVG()`, `MAX()`, `MIN()`, `GROUP BY`, `HAVING`.
   - Subqueries and subselect filtering.

## Mini Project
Design 6-table 3NF relational database schema for the Disaster Management System (`roles`, `users`, `shelters`, `disaster_reports`, `resource_requests`, `incident_logs`), craft DDL (`schema.sql`), seed data (`data.sql`), and write operational queries (`queries.sql`).

## Integration into Disaster Management System
Location: `database/` and `backend/disaster-api/src/main/resources/`
- `schema.sql`: Contains production DDL statements with foreign key constraints.
- `data.sql`: Seed data for system roles (`ROLE_CITIZEN`, `ROLE_VOLUNTEER`, `ROLE_RESCUE_TEAM`, `ROLE_ADMIN`), initial shelter camps, and sample disaster reports.
- `queries.sql`: Operational queries for capacity calculation, crisis aggregation, and incident tracking.

## Git Commit
`feat(sql): create 3NF relational database schema.sql, data.sql seed scripts, and operational queries`

## Interview Questions
1. *What is Third Normal Form (3NF) and why is it important?*
   - A table is in 3NF if it is in 2NF and has no transitive functional dependencies (non-key columns must depend strictly on the primary key). This eliminates data redundancy and update anomalies.
2. *What is the difference between `WHERE` and `HAVING` clauses in SQL?*
   - `WHERE` filters individual rows *before* grouping occurs. `HAVING` filters aggregated groups *after* `GROUP BY` execution.

## Completion Checklist
- [x] Designed 6-table 3NF ER database model
- [x] Written DDL `schema.sql` with foreign keys and cascade rules
- [x] Written DML `data.sql` with seed roles, users, and shelters
- [x] Created `queries.sql` with analytical aggregation queries
