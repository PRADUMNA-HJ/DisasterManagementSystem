# Disaster Management System - Database Design Documentation

## 1. Overview
This document outlines the Phase 1 relational database design for the **Disaster Management System**. The database schema is designed following enterprise relational database design practices, normalized to Third Normal Form (3NF), and built to support seamless transition from a modular monolith to a microservices architecture.

---

## 2. Entity & Table Descriptions

### 2.1 `roles`
Defines system authority levels and access control roles across the application.
- **`id`** (BIGINT, PK, AUTO_INCREMENT): Unique identifier.
- **`name`** (VARCHAR(30), UNIQUE, NOT NULL): Role name enum (`CITIZEN`, `VOLUNTEER`, `RESCUE_TEAM`, `ADMIN`).

### 2.2 `users`
Stores user profile credentials, contact information, and role bindings.
- **`id`** (BIGINT, PK, AUTO_INCREMENT): Unique user identifier.
- **`full_name`** (VARCHAR(100), NOT NULL): User's legal or displayed name.
- **`email`** (VARCHAR(150), UNIQUE, NOT NULL): Primary login identifier.
- **`password`** (VARCHAR(255), NOT NULL): Encrypted user credentials (plain text in Phase 1).
- **`phone`** (VARCHAR(20), NOT NULL): Emergency contact telephone number.
- **`city`** (VARCHAR(100), NOT NULL): User's primary geographic city.
- **`enabled`** (BOOLEAN, NOT NULL): Account active status flag.
- **`created_at`** (DATETIME, NOT NULL): Registration timestamp.
- **`role_id`** (BIGINT, FK -> `roles.id`, NOT NULL): Foreign key referencing the user's role.

### 2.3 `shelters`
Tracks emergency relief camps, safe houses, and temporary shelter facilities.
- **`id`** (BIGINT, PK, AUTO_INCREMENT): Unique shelter identifier.
- **`name`** (VARCHAR(150), NOT NULL): Shelter facility name.
- **`address`** (VARCHAR(255), NOT NULL): Street address.
- **`city`** (VARCHAR(100), NOT NULL): City location.
- **`capacity`** (INT, NOT NULL): Maximum total occupancy limit.
- **`occupied`** (INT, NOT NULL): Currently occupied bed count.
- **`latitude`** (DOUBLE PRECISION): GPS latitude coordinate.
- **`longitude`** (DOUBLE PRECISION): GPS longitude coordinate.

### 2.4 `disaster_reports`
Core incident table storing disaster event reports filed by citizens and authorities.
- **`id`** (BIGINT, PK, AUTO_INCREMENT): Unique disaster report identifier.
- **`title`** (VARCHAR(255), NOT NULL): Summary headline of disaster event.
- **`description`** (TEXT): Detailed situation report and observations.
- **`location`** (VARCHAR(255), NOT NULL): Disaster site location description.
- **`disaster_type`** (VARCHAR(100), NOT NULL): Category (e.g., FLOOD, FIRE, LANDSLIDE, CYCLONE).
- **`severity`** (VARCHAR(50), NOT NULL): Criticality rating (LOW, MEDIUM, HIGH, CRITICAL).
- **`reported_at`** (DATETIME, NOT NULL): Timestamp when incident was filed.

### 2.5 `resource_requests`
Manages requests for critical relief supplies submitted during an active disaster.
- **`id`** (BIGINT, PK, AUTO_INCREMENT): Unique request identifier.
- **`resource_type`** (VARCHAR(30), NOT NULL): Resource enum (`FOOD`, `WATER`, `MEDICINE`, `CLOTHES`).
- **`quantity`** (INT, NOT NULL): Requested item count.
- **`status`** (VARCHAR(30), NOT NULL): Request status enum (`PENDING`, `APPROVED`, `DELIVERED`).
- **`requested_at`** (DATETIME, NOT NULL): Timestamp when request was submitted.
- **`user_id`** (BIGINT, FK -> `users.id`, NOT NULL): User who initiated the request.
- **`disaster_report_id`** (BIGINT, FK -> `disaster_reports.id`, NOT NULL): Associated disaster report.

### 2.6 `incident_logs`
Chronological event audit trail tracking actions taken during disaster response operations.
- **`id`** (BIGINT, PK, AUTO_INCREMENT): Unique log identifier.
- **`message`** (VARCHAR(500), NOT NULL): Descriptive update (e.g., "Rescue team dispatched").
- **`created_at`** (DATETIME, NOT NULL): Timestamp when update occurred.
- **`disaster_report_id`** (BIGINT, FK -> `disaster_reports.id`, NOT NULL): Target disaster report.

---

## 3. Relationships & Cardinality

| Source Entity | Target Entity | Relationship Type | Cardinality | Foreign Key | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Role** | **User** | One-to-Many | `1 : M` | `users.role_id` | One role can be assigned to many users; each user has exactly 1 role. |
| **User** | **ResourceRequest** | One-to-Many | `1 : M` | `resource_requests.user_id` | A user can initiate multiple resource requests over time. |
| **DisasterReport** | **ResourceRequest** | One-to-Many | `1 : M` | `resource_requests.disaster_report_id` | A disaster report can have multiple associated relief requests. |
| **DisasterReport** | **IncidentLog** | One-to-Many | `1 : M` | `incident_logs.disaster_report_id` | A disaster report accumulates a timeline of operational incident logs. |
| **Shelter** | *Independent* | N/A | Standalone | N/A | Shelters operate independently by city/coordinates for capacity tracking. |

---

## 4. ASCII Entity-Relationship (ER) Diagram

```text
+-------------------+             +----------------------------------+
|       ROLES       |             |              USERS               |
+-------------------+             +----------------------------------+
| PK  id            |<-------+    | PK  id                           |
|     name          |        |    |     full_name                    |
+-------------------+        |    |     email (UNIQUE)               |
                             |    |     password                     |
                             |    |     phone                        |
                             |    |     city                         |
                             |    |     enabled                      |
                             |    |     created_at                   |
                             +----| FK  role_id                      |
                                  +----------------------------------+
                                                   |
                                                   | 1
                                                   |
                                                   | M
+-------------------+             +----------------------------------+
|     SHELTERS      |             |        RESOURCE_REQUESTS         |
+-------------------+             +----------------------------------+
| PK  id            |             | PK  id                           |
|     name          |             |     resource_type                |
|     address       |             |     quantity                     |
|     city          |             |     status                       |
|     capacity      |             |     requested_at                 |
|     occupied      |      +----->| FK  user_id                      |
|     latitude      |      |   +--| FK  disaster_report_id           |
|     longitude     |      |   |  +----------------------------------+
+-------------------+      |   |
                           |   |
                           |   | 
+----------------------------------+             +----------------------------------+
|         DISASTER_REPORTS         |             |          INCIDENT_LOGS           |
+----------------------------------+             +----------------------------------+
| PK  id                           |             | PK  id                           |
|     title                        |             |     message                      |
|     description                  |             |     created_at                   |
|     location                     |             | FK  disaster_report_id           |<---+
|     disaster_type                |             +----------------------------------+    |
|     severity                     |                                                     |
|     reported_at                  |-----------------------------------------------------+
+----------------------------------+ 1                                                 M
```
