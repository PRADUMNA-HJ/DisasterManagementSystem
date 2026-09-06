# Learning Notes: 08 - Relational SQL & Database Schema Normalization

## Overview
Structured Query Language (SQL) is the foundational language used to define and manipulate data in relational databases (MySQL, H2) for the **Disaster Management System**.

---

## 1. Third Normal Form (3NF) Normalization

The database schema is normalized to **3NF** to prevent redundant data storage, insertion anomalies, update anomalies, and deletion anomalies.

- **1NF (Atomic Attributes)**: Every table cell contains single scalar values.
- **2NF (No Partial Key Dependencies)**: Every non-key column depends on the complete primary key (`id`).
- **3NF (No Transitive Dependencies)**: Non-key columns do not depend on other non-key columns (e.g., user role names isolated in `roles` table, referenced via `users.role_id`).

---

## 2. DDL Schema Definition (`schema.sql`)

```sql
CREATE TABLE disaster_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255) NOT NULL,
    disaster_type VARCHAR(100) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    reported_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE resource_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_type VARCHAR(30) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 1),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    disaster_report_id BIGINT NOT NULL,
    CONSTRAINT fk_res_req_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_res_req_disaster FOREIGN KEY (disaster_report_id) REFERENCES disaster_reports(id) ON DELETE CASCADE
);
```

---

## 3. Analytical & Operational SQL Queries

### Shelter Occupancy & Available Bed Capacity:
```sql
SELECT 
    name, 
    city, 
    capacity, 
    occupied, 
    (capacity - occupied) AS available_beds
FROM shelters
WHERE city = 'Bangalore' AND (capacity - occupied) > 0
ORDER BY available_beds DESC;
```

### Relational Join & Aggregation:
```sql
SELECT 
    dr.id AS disaster_id,
    dr.title,
    COUNT(rr.id) AS total_resource_requests
FROM disaster_reports dr
LEFT JOIN resource_requests rr ON dr.id = rr.disaster_report_id
GROUP BY dr.id, dr.title
ORDER BY total_resource_requests DESC;
```

---

## 4. Key Takeaways
- Foreign key constraints enforce referential integrity across related domain entities.
- 3NF design minimizes table redundancy and maintains data consistency across updates.
- SQL aggregation functions (`SUM`, `COUNT`, `GROUP BY`) power real-time disaster dashboard stats.
