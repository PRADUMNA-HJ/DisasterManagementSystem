# Docker Deployment Guide

## Prerequisites
- Docker Desktop 24+ (Windows/macOS) or Docker Engine 24+ (Linux)
- Docker Compose v2.20+

## Quick Start

### 1. Clone and Configure Environment
```bash
git clone https://github.com/your-org/DisasterManagementSystem.git
cd DisasterManagementSystem

# Create .env file from template
cp env/.env.example env/.env
# Edit env/.env with your DB credentials and JWT secret
```

### 2. Start All Services
```bash
# From project root
docker compose -f docker/docker-compose.yml up -d
```

### 3. Verify Running Containers
```bash
docker compose -f docker/docker-compose.yml ps
```

Expected output:
```text
NAME              IMAGE                STATUS         PORTS
disaster-db       mysql:8.0            Up (healthy)   0.0.0.0:3306->3306/tcp
disaster-api      disaster-api:latest  Up             0.0.0.0:8080->8080/tcp
```

### 4. Access the API
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 5. View Logs
```bash
# Stream backend logs
docker logs disaster-api -f

# Stream database logs
docker logs disaster-db -f
```

### 6. Stop & Clean Up
```bash
# Stop services (preserve volumes)
docker compose -f docker/docker-compose.yml down

# Stop and remove volumes (clean slate)
docker compose -f docker/docker-compose.yml down -v
```

## Build Backend Image Manually
```bash
cd backend/disaster-api
docker build -f ../../docker/Dockerfile.backend -t disaster-api:latest .
```
