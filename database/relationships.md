# Disaster Management System - Database Relationships & Schema Analysis

## 1. Relational Entity Overview

The relational database model comprises 6 entities normalized to **Third Normal Form (3NF)**:
1. `roles`
2. `users`
3. `shelters`
4. `disaster_reports`
5. `resource_requests`
6. `incident_logs`

---

## 2. Table Relationships & Foreign Keys

### 2.1 `roles` <---> `users`
- **Relationship Type**: One-to-Many (`1 : M`)
- **Foreign Key**: `users.role_id` referencing `roles.id`
- **Cardinality**: One role can be assigned to multiple users; each user must possess exactly 1 role.
- **Referential Integrity**: `ON DELETE RESTRICT ON UPDATE CASCADE` (prevents deleting active system roles).

### 2.2 `users` <---> `resource_requests`
- **Relationship Type**: One-to-Many (`1 : M`)
- **Foreign Key**: `resource_requests.user_id` referencing `users.id`
- **Cardinality**: A registered user can initiate multiple relief resource requests.
- **Referential Integrity**: `ON DELETE CASCADE` (removes requests if user account is purged).

### 2.3 `disaster_reports` <---> `resource_requests`
- **Relationship Type**: One-to-Many (`1 : M`)
- **Foreign Key**: `resource_requests.disaster_report_id` referencing `disaster_reports.id`
- **Cardinality**: A single disaster incident report can have multiple relief supply requests associated with it.
- **Referential Integrity**: `ON DELETE CASCADE` (deleting disaster report clears related resource requests).

### 2.4 `disaster_reports` <---> `incident_logs`
- **Relationship Type**: One-to-Many (`1 : M`)
- **Foreign Key**: `incident_logs.disaster_report_id` referencing `disaster_reports.id`
- **Cardinality**: A disaster report accumulates a chronological audit history of operational logs.
- **Referential Integrity**: `ON DELETE CASCADE`.

### 2.5 `shelters` (Independent Entity)
- **Relationship Type**: Standalone Entity
- **Description**: Tracks location, capacity, and occupancy for emergency camps across cities without hard coupling to specific disaster reports.

---

## 3. Normalization Verification (3NF Compliance)

- **1NF**: All table columns contain atomic, primitive values (no multi-valued attributes or repeating groups).
- **2NF**: All non-key attributes are fully functionally dependent on the entire primary key (`id`).
- **3NF**: No transitive dependencies exist; non-key attributes depend strictly on the primary key alone.
