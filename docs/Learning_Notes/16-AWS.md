# Learning Notes: 16 - AWS Cloud Deployment & Production Architecture

## Overview
Amazon Web Services (AWS) provides the managed cloud infrastructure to host the **Disaster Management System** in a production environment with high availability, security, and scalability.

---

## 1. Production AWS Architecture

```text
Internet
    |
    v
+-------------------+
| Route 53 (DNS)    |  disaster-api.example.com --> ALB
+-------------------+
    |
    v
+-------------------+
| ALB (HTTPS/443)   |  SSL Certificate via ACM
+-------------------+
    |
    v
+----------------------------------------------+
|                Public Subnet                  |
|  +------------------+  +------------------+  |
|  | EC2 Instance 1   |  | EC2 Instance 2   |  |
|  | Spring Boot API  |  | Spring Boot API  |  |
|  | (Docker)         |  | (Docker)         |  |
|  +------------------+  +------------------+  |
+----------------------------------------------+
    |
    v
+----------------------------------------------+
|               Private Subnet                  |
|  +------------------------------------------+|
|  | RDS MySQL 8.0 (Multi-AZ)                 ||
|  | Automated backups, read replicas          ||
|  +------------------------------------------+|
+----------------------------------------------+
```

---

## 2. Key Services Configuration

### RDS MySQL Configuration
```properties
# Spring Boot production datasource (credentials from SSM)
spring.datasource.url=jdbc:mysql://${RDS_ENDPOINT}:3306/disaster_db?useSSL=true
spring.datasource.username=${RDS_USERNAME}
spring.datasource.password=${RDS_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
```

### CloudWatch Logging (application.properties)
```properties
logging.level.com.disastermanagement=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 3. GitHub Actions CI/CD Pipeline

```yaml
name: Build and Deploy

on:
  push:
    branches: [ main ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Build Docker Image
        run: docker build -f docker/Dockerfile.backend -t disaster-api:${{ github.sha }} .
      - name: Push to ECR
        run: |
          aws ecr get-login-password | docker login --username AWS --password-stdin $ECR_URI
          docker push $ECR_URI/disaster-api:${{ github.sha }}
```

---

## 4. Key Takeaways
- RDS in a private subnet with security group rules is far more secure than a self-managed MySQL EC2 instance.
- IAM instance profiles eliminate the need for embedded AWS credentials in application code.
- Multi-AZ RDS provides automatic failover within minutes if the primary database instance fails.
