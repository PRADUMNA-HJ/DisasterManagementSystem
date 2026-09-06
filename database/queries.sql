-- ====================================================================
-- Disaster Management System - Operational SQL Queries & Analytics
-- Compatible with MySQL 8.0+ and H2 Database
-- ====================================================================

-- 1. Find all active shelters with available capacity in a specific city
SELECT 
    id, 
    name, 
    address, 
    city, 
    capacity, 
    occupied, 
    (capacity - occupied) AS available_beds
FROM shelters
WHERE city = 'Bangalore' AND (capacity - occupied) > 0
ORDER BY available_beds DESC;

-- 2. Retrieve critical and high severity disaster reports reported in the last 7 days
SELECT 
    id, 
    title, 
    location, 
    disaster_type, 
    severity, 
    reported_at
FROM disaster_reports
WHERE severity IN ('HIGH', 'CRITICAL')
ORDER BY reported_at DESC;

-- 3. Calculate total requested resource quantity aggregated by resource type
SELECT 
    resource_type, 
    status, 
    COUNT(id) AS total_requests, 
    SUM(quantity) AS total_items_requested
FROM resource_requests
GROUP BY resource_type, status
ORDER BY resource_type, status;

-- 4. Get disaster reports with associated resource request counts and incident log counts
SELECT 
    dr.id AS disaster_id,
    dr.title,
    dr.severity,
    COUNT(DISTINCT rr.id) AS total_resource_requests,
    COUNT(DISTINCT il.id) AS total_incident_logs
FROM disaster_reports dr
LEFT JOIN resource_requests rr ON dr.id = rr.disaster_report_id
LEFT JOIN incident_logs il ON dr.id = il.disaster_report_id
GROUP BY dr.id, dr.title, dr.severity
ORDER BY dr.id DESC;

-- 5. List users registered as VOLUNTEER or RESCUE_TEAM along with their roles
SELECT 
    u.id, 
    u.full_name, 
    u.email, 
    u.phone, 
    u.city, 
    r.name AS role_name
FROM users u
JOIN roles r ON u.role_id = r.id
WHERE r.name IN ('ROLE_VOLUNTEER', 'ROLE_RESCUE_TEAM')
ORDER BY u.full_name ASC;

-- 6. Retrieve incident audit timeline for a specific disaster report
SELECT 
    il.id AS log_id,
    il.message,
    il.created_at,
    dr.title AS disaster_title
FROM incident_logs il
JOIN disaster_reports dr ON il.disaster_report_id = dr.id
WHERE dr.id = 1
ORDER BY il.created_at ASC;
