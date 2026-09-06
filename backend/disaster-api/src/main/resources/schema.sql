-- ====================================================================
-- Disaster Management System - Database Schema (DDL)
-- Compatible with MySQL 8.0+ and H2 Database
-- ====================================================================

DROP TABLE IF EXISTS incident_logs;
DROP TABLE IF EXISTS resource_requests;
DROP TABLE IF EXISTS disaster_reports;
DROP TABLE IF EXISTS shelters;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- 1. Roles Table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

-- 2. Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    city VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 3. Shelters Table
CREATE TABLE shelters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    capacity INT NOT NULL CHECK (capacity >= 0),
    occupied INT NOT NULL DEFAULT 0 CHECK (occupied >= 0),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

-- 4. Disaster Reports Table
CREATE TABLE disaster_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255) NOT NULL,
    disaster_type VARCHAR(100) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    reported_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Resource Requests Table
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

-- 6. Incident Logs Table
CREATE TABLE incident_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message VARCHAR(500) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    disaster_report_id BIGINT NOT NULL,
    CONSTRAINT fk_inc_logs_disaster FOREIGN KEY (disaster_report_id) REFERENCES disaster_reports(id) ON DELETE CASCADE
);
