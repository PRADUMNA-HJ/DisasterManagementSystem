-- ====================================================================
-- Disaster Management System - Database Seed Data (DML)
-- Realistic initial dataset for local testing and development
-- ====================================================================

-- 1. Insert System Roles
INSERT INTO roles (id, name) VALUES (1, 'ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'CITIZEN');
INSERT INTO roles (id, name) VALUES (3, 'VOLUNTEER');
INSERT INTO roles (id, name) VALUES (4, 'RESCUE_TEAM');

-- 2. Insert Initial Users
INSERT INTO users (id, full_name, email, password, phone, city, enabled, created_at, role_id) 
VALUES (1, 'Admin User', 'admin@disaster.org', 'admin123', '+91-9876543210', 'Bangalore', TRUE, CURRENT_TIMESTAMP, 1);

INSERT INTO users (id, full_name, email, password, phone, city, enabled, created_at, role_id) 
VALUES (2, 'Rahul Sharma', 'rahul.sharma@gmail.com', 'citizen123', '+91-9876543211', 'Bangalore', TRUE, CURRENT_TIMESTAMP, 2);

INSERT INTO users (id, full_name, email, password, phone, city, enabled, created_at, role_id) 
VALUES (3, 'Anita Singh', 'anita.singh@gmail.com', 'volunteer123', '+91-9876543212', 'Chennai', TRUE, CURRENT_TIMESTAMP, 3);

INSERT INTO users (id, full_name, email, password, phone, city, enabled, created_at, role_id) 
VALUES (4, 'Captain Verma', 'captain.verma@ndrf.gov.in', 'rescue123', '+91-9876543213', 'Wayanad', TRUE, CURRENT_TIMESTAMP, 4);

-- 3. Insert Shelters
INSERT INTO shelters (id, name, address, city, capacity, occupied, latitude, longitude) 
VALUES (1, 'Bangalore Relief Camp', 'Indiranagar Community Hall, Stage 2', 'Bangalore', 500, 120, 12.9715987, 77.5945627);

INSERT INTO shelters (id, name, address, city, capacity, occupied, latitude, longitude) 
VALUES (2, 'Whitefield School Shelter', 'Government High School Road, Whitefield', 'Bangalore', 300, 45, 12.9698100, 77.7499000);

-- 4. Insert Disaster Reports
INSERT INTO disaster_reports (id, title, description, location, disaster_type, severity, reported_at) 
VALUES (1, 'Urban Flood Inundation', 'Heavy rainfall caused severe flooding in low-lying residential areas.', 'Bangalore', 'FLOOD', 'HIGH', CURRENT_TIMESTAMP);

INSERT INTO disaster_reports (id, title, description, location, disaster_type, severity, reported_at) 
VALUES (2, 'Commercial Complex Fire Hazard', 'Electrical short circuit triggered major fire outbreak in market area.', 'Chennai', 'FIRE', 'CRITICAL', CURRENT_TIMESTAMP);

INSERT INTO disaster_reports (id, title, description, location, disaster_type, severity, reported_at) 
VALUES (3, 'Highway Mudslide & Landslide', 'Continuous heavy downpour caused hill collapse blocking state highway.', 'Wayanad', 'LANDSLIDE', 'HIGH', CURRENT_TIMESTAMP);

-- 5. Insert Resource Requests
INSERT INTO resource_requests (id, resource_type, quantity, status, requested_at, user_id, disaster_report_id) 
VALUES (1, 'FOOD', 150, 'PENDING', CURRENT_TIMESTAMP, 2, 1);

INSERT INTO resource_requests (id, resource_type, quantity, status, requested_at, user_id, disaster_report_id) 
VALUES (2, 'WATER', 300, 'APPROVED', CURRENT_TIMESTAMP, 3, 1);

INSERT INTO resource_requests (id, resource_type, quantity, status, requested_at, user_id, disaster_report_id) 
VALUES (3, 'MEDICINE', 50, 'DELIVERED', CURRENT_TIMESTAMP, 4, 3);

-- 6. Insert Incident Logs
INSERT INTO incident_logs (id, message, created_at, disaster_report_id) 
VALUES (1, 'Report received from citizen hotline and logged into central dispatch.', CURRENT_TIMESTAMP, 1);

INSERT INTO incident_logs (id, message, created_at, disaster_report_id) 
VALUES (2, 'Verification completed by field survey team.', CURRENT_TIMESTAMP, 1);

INSERT INTO incident_logs (id, message, created_at, disaster_report_id) 
VALUES (3, 'Rescue team dispatched with boat support and medical kits.', CURRENT_TIMESTAMP, 1);
