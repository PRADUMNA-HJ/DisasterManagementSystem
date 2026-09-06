# Disaster Management System - Operational Incident Lifecycle

## Workflow Overview

```text
  +-------------------+
  | 1. Incident Filing| ---> Citizen/Rescue Team posts new disaster report (Title, Location, Severity)
  +-------------------+
            |
            v
  +-------------------+
  | 2. Verification   | ---> ADMIN / RESCUE_TEAM reviews report, updates status, logs initial incident update
  +-------------------+
            |
            v
  +-------------------+
  | 3. Relief Ops     | ---> Submit resource requests (FOOD, WATER, MEDICINE) & assign safe shelters
  +-------------------+
            |
            v
  +-------------------+
  | 4. Fulfillment    | ---> Volunteers/Rescue Teams update resource request status (APPROVED -> DELIVERED)
  +-------------------+
            |
            v
  +-------------------+
  | 5. Resolution     | ---> Incident closed, operational audit log maintained in incident_logs table
  +-------------------+
```

## Key Operational Scenarios

1. **Citizen Disaster Reporting**:
   - Registered citizens submit incident report (`/api/v1/disasters`).
   - Incident severity level set (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`).

2. **Shelter Capacity Management**:
   - Safe shelters registered with maximum capacity and current occupancy count.
   - Real-time availability computed (`capacity - occupied`).

3. **Resource Request & Supply Chain**:
   - Crisis areas submit requests for essential supplies (`FOOD`, `WATER`, `MEDICINE`, `CLOTHES`).
   - Request lifecycle transitions: `PENDING` -> `APPROVED` -> `DELIVERED`.

4. **Incident Audit Trail**:
   - Detailed event updates recorded in `incident_logs` with disaster report reference and timestamps.
