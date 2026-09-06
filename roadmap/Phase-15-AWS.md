# Phase-15: AWS Cloud Deployment & Production Infrastructure

## Objectives
- Understand core AWS services relevant to deploying Spring Boot microservices in production.
- Deploy the Disaster Management System backend on EC2 with RDS MySQL for managed database hosting.
- Configure security groups, VPC networking, and IAM roles for secure AWS resource access.
- Set up CloudWatch logging, alarms, and S3 for media or log artifact storage.

## Topics
1. **Core AWS Services**:
   - **EC2 (Elastic Compute Cloud)**: Virtual server instances running the Dockerized Spring Boot application.
   - **RDS (Relational Database Service)**: Managed MySQL 8.0 database with automated backups, Multi-AZ failover, and read replicas.
   - **S3 (Simple Storage Service)**: Object storage for static media assets, log archives, and Terraform state.
   - **IAM (Identity and Access Management)**: Roles, policies, and instance profiles controlling resource access without hardcoded credentials.
   - **CloudWatch**: Metrics collection, log aggregation, custom alarms, and dashboards.
   - **VPC (Virtual Private Cloud)**: Isolated network with public subnets (EC2, ALB) and private subnets (RDS).
2. **Deployment Architecture**:
   - **Application Load Balancer (ALB)**: Distributes HTTP/S traffic across EC2 instances in an Auto Scaling Group.
   - **Auto Scaling Group**: Horizontally scales EC2 instances based on CloudWatch CPU/memory metrics.
   - **RDS in Private Subnet**: Database not directly exposed to internet; accessible only from EC2 security group.
3. **CI/CD Pipeline** (GitHub Actions):
   - On push to `main`: Build Docker image, push to ECR, trigger EC2 deployment.
4. **Environment Variable Management on AWS**:
   - Store DB passwords and JWT secrets in **AWS Systems Manager Parameter Store** (SSM) or **Secrets Manager**.
   - EC2 instance profiles with IAM roles fetch secrets at runtime.

## Mini Project
Design a production AWS deployment topology for the Disaster Management System: EC2 + Docker, RDS MySQL, ALB, CloudWatch logging, and GitHub Actions CI/CD pipeline.

## Integration into Disaster Management System
Location: `docs/Deployment/` and `.github/workflows/`
- AWS infrastructure provisioned using `docker-compose` for development and Terraform (IaC) for production.
- `docker/docker-compose.yml` used for local and CI validation; AWS ECS Fargate or EC2 for production.

## Git Commit
`docs(aws): document production AWS deployment topology, RDS setup, ALB, and CI/CD pipeline`

## Interview Questions
1. *Why should a database (RDS) be placed in a private subnet in an AWS VPC?*
   - Private subnets have no route to the internet gateway, preventing direct external access to the database. Only resources within the VPC (like EC2 instances in a specific security group) can reach RDS, significantly reducing the attack surface.
2. *What is the difference between AWS IAM Roles and IAM Users for EC2 deployments?*
   - IAM Users use long-lived access keys (security risk if leaked). IAM Roles assigned to EC2 instance profiles provide temporary, automatically rotated credentials via the EC2 metadata service — the preferred and secure approach for machine-to-AWS service authentication.

## Completion Checklist
- [x] Designed 3-tier VPC architecture (public ALB, private EC2/ECS, isolated RDS subnet)
- [x] Documented RDS MySQL configuration with Multi-AZ and automated backups
- [x] Documented IAM instance profiles eliminating hardcoded AWS credentials
- [x] Outlined GitHub Actions CI/CD pipeline for automated Docker build and deployment
