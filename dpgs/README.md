# Digital Payment Gateway Simulator

A production-inspired backend application built using **Java 21**, **Spring Boot 3.5.x**, **Spring Security**, **JWT Authentication**, and **PostgreSQL** that simulates a real-world Digital Payment Gateway.

The project follows enterprise backend development practices including secure authentication, authorization, role-based access control, order management, payment processing, webhook simulation, scheduled jobs, notifications, audit logging, request logging, exception handling, Docker containerization, Docker Compose, and Git feature-based development.

---

# 🚀 Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Security
- JWT Authentication
- Spring Data JPA (Hibernate)
- PostgreSQL 16
- Maven
- Lombok
- SLF4J + Logback
- Spring Scheduler
- Docker
- Docker Compose
- Git & GitHub

---

# ✨ Features

## 🔐 Authentication

- User Registration
- User Login
- BCrypt Password Encryption
- JWT Token Generation
- JWT Authentication Filter
- Stateless Authentication
- Protected APIs

---

## 👥 Role Based Access Control (RBAC)

### Roles

- USER
- ADMIN

### Access Rules

| Endpoint | USER | ADMIN |
|----------|:----:|:-----:|
| `/api/auth/**` | ✅ | ✅ |
| `/api/orders/**` | ✅ | ✅ |
| `/api/payments/**` | ✅ | ✅ |
| `/api/notifications/**` | ✅ | ✅ |
| `/api/admin/**` | ❌ | ✅ |

### JWT Claims

JWT tokens contain:

- Email
- Role

Example:

```json
{
  "sub": "admin@gmail.com",
  "role": "ADMIN"
}
````

---

## 📦 Order Management

* Create Order
* Get Order By Order ID
* Get Logged-in User Orders
* Order Ownership Validation
* Order Status Tracking

### Order Lifecycle

```text
CREATED
    │
    ▼
PAYMENT_PENDING
   ├──────────────► PAID
   │
   └──────────────► FAILED
```

---

## 💳 Payment Management

### Payment APIs

* Initiate Payment
* Mark Payment Success
* Mark Payment Failed
* Get Payment Details
* Get Payments By Order

### Payment Validations

* Duplicate Payment Prevention
* Payment Ownership Validation
* Payment State Validation
* Prevent Re-processing of Completed Payments

### Payment Features

* Payment Idempotency
* Payment Ownership Validation
* Payment State Validation
* Payment Expiry Scheduler
* Webhook Payment Processing
* Audit Tracking

---

## 🔄 Webhook Simulation

Simulates payment gateway callbacks similar to real-world providers such as Razorpay, Stripe, or Cashfree.

### APIs

```http
POST /api/webhooks/payment-success
POST /api/webhooks/payment-failed
```

### Features

* Processes asynchronous payment callbacks
* Updates Payment Status
* Updates Order Status
* Generates Notifications
* Creates Audit Records
* Prevents Duplicate Processing

---

## ⏰ Scheduled Jobs

Implemented automatic payment expiration using Spring Scheduler.

### Current Job

* Expires pending payments after configured timeout
* Updates Payment Status
* Updates Order Status
* Creates Audit Records

---

## 🔔 Notification Module

* Notification Entity
* Notification Service
* Notification Repository
* Get Logged-in User Notifications
* Automatic Notification Creation on Successful Payment

---

## 📧 Mock Email Service

A mock email service is implemented to simulate real-world email delivery.

### Features

* Payment Success Email Simulation
* Structured Email Logs
* Notification Integration
* Audit Tracking

Example Log:

```text
========== EMAIL SENT ==========
To      : user@gmail.com
Subject : Payment Successful
Body    : Your payment was completed successfully.
================================
```

---

## 📋 Audit Module

Tracks important business events throughout the payment lifecycle.

### Implemented Audit Events

* PAYMENT_CREATED
* PAYMENT_PENDING
* PAYMENT_SUCCESS
* PAYMENT_FAILED
* PAYMENT_EXPIRED
* NOTIFICATION_SENT
* EMAIL_SENT

---

## 👨‍💼 Admin Module

Administrative APIs are protected using `ROLE_ADMIN`.

### Implemented APIs

#### Get All Payments

```http
GET /api/admin/payments
```

### Features

* Admin-only access
* View all payments
* System-wide payment visibility
* Payment monitoring

---

## 📝 Logging

Application-wide logging using **SLF4J + Logback**.

### Logging Implemented

* Authentication Logs
* Order Logs
* Payment Logs
* Notification Logs
* Email Logs
* Webhook Logs
* Scheduler Logs
* Request Logs
* Error Logs
* Unauthorized Access Logs

---

## 🛡 Security

* Spring Security Filter Chain
* JWT Authentication
* Role-Based Access Control (RBAC)
* Resource Ownership Validation
* Stateless Authentication
* Global Exception Handling
* Custom Exceptions
* BCrypt Password Encoding

---

# 🐳 Docker

The application is containerized using Docker.

The Docker setup runs the following services:

```text
Docker Compose
      │
      ├── dpgs-app
      │     └── Spring Boot + Java 21
      │
      └── dpgs-postgres
            └── PostgreSQL 16
```

The Spring Boot application and PostgreSQL database run in separate containers and communicate through the Docker Compose network.

---

## Dockerfile

The DPGS application uses the following Dockerfile:

```dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/dpgs-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Dockerfile Instructions

| Instruction  | Purpose                                         |
| ------------ | ----------------------------------------------- |
| `FROM`       | Defines the base Java 21 image                  |
| `WORKDIR`    | Sets the working directory inside the container |
| `COPY`       | Copies the Spring Boot JAR into the image       |
| `EXPOSE`     | Documents application port `8080`               |
| `ENTRYPOINT` | Defines the application startup command         |

---

# 🐘 Docker Compose

Docker Compose is used to run the Spring Boot application and PostgreSQL together.

The Compose architecture is:

```text
                    Docker Compose
                          │
             ┌────────────┴────────────┐
             │                         │
             ▼                         ▼
      ┌──────────────┐         ┌───────────────┐
      │   dpgs-app   │         │ dpgs-postgres │
      │              │         │               │
      │ Spring Boot  │────────►│ PostgreSQL 16 │
      │ Java 21      │ Network │               │
      │ Port 8080    │         │ Port 5432     │
      └──────────────┘         └───────┬───────┘
                                       │
                                       ▼
                                postgres_data
                                   Volume
```

---

## Docker Compose Configuration

```yaml
services:

  postgres:
    image: postgres:16
    container_name: dpgs-postgres
    environment:
      POSTGRES_DB: dpgs
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: admin
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  dpgs:
    build: .
    container_name: dpgs-app
    depends_on:
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: docker
    ports:
      - "8080:8080"

volumes:
  postgres_data:
```

---

# 🔗 Docker Networking

When running inside Docker Compose, the Spring Boot application connects to PostgreSQL using the Compose service name.

Docker configuration:

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/dpgs
spring.datasource.username=postgres
spring.datasource.password=admin
```

The important part is:

```text
postgres
```

which is the PostgreSQL service name defined in `docker-compose.yml`.

Inside the Spring Boot container:

```text
localhost
```

refers to the Spring Boot container itself.

Therefore, PostgreSQL is accessed using:

```text
postgres:5432
```

instead of:

```text
localhost:5432
```

---

# 💾 PostgreSQL Persistence

PostgreSQL uses a Docker named volume:

```text
postgres_data
```

The volume is mounted to:

```text
/var/lib/postgresql/data
```

This ensures database data persists even when the PostgreSQL container is recreated.

### Persistence Flow

```text
PostgreSQL Container
        │
        ▼
postgres_data Volume
        │
        ▼
Persistent Database Data
```

### Important

```bash
docker compose down
```

removes the containers and network but keeps the named volume.

However:

```bash
docker compose down -v
```

also removes the volume and its PostgreSQL data.

---

# 🏗 Project Structure

```text
src/main/java/com/balmik/dpgs

├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── filter
├── repository
├── security
├── scheduler
├── service
│   └── impl
└── DpgsApplication
```

---

# 📚 REST APIs

## Authentication

### Register User

```http
POST /api/auth/register
```

Request:

```json
{
  "name": "Balmik",
  "email": "balmik@gmail.com",
  "password": "password123"
}
```

---

### Login

```http
POST /api/auth/login
```

Request:

```json
{
  "email": "balmik@gmail.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "JWT_TOKEN"
}
```

---

# 📦 Orders

## Create Order

```http
POST /api/orders
```

Headers:

```http
Authorization: Bearer <JWT_TOKEN>
```

Request:

```json
{
  "amount": 5000,
  "description": "Laptop Purchase"
}
```

---

## Get Order By ID

```http
GET /api/orders/{orderId}
```

---

## Get My Orders

```http
GET /api/orders/my-orders
```

---

# 💳 Payments

## Initiate Payment

```http
POST /api/payments/initiatePayment
```

Request:

```json
{
  "orderId": "ORD-1783358727708",
  "paymentMethod": "UPI"
}
```

---

## Mark Payment Success

```http
POST /api/payments/{paymentId}/success
```

---

## Mark Payment Failed

```http
POST /api/payments/{paymentId}/fail
```

---

## Get Payment

```http
GET /api/payments/getPayment/{paymentId}
```

---

## Get Payments By Order

```http
GET /api/payments/order/{orderId}
```

---

# 🔔 Notifications

## Get Logged-in User Notifications

```http
GET /api/notifications/my-notifications
```

---

# 🔄 Webhooks

## Payment Success Callback

```http
POST /api/webhooks/payment-success
```

Request:

```json
{
  "paymentId": "PAY-1785046301509"
}
```

---

## Payment Failed Callback

```http
POST /api/webhooks/payment-failed
```

Request:

```json
{
  "paymentId": "PAY-1785046301509"
}
```

---

# 👨‍💼 Admin APIs

## Get All Payments

```http
GET /api/admin/payments
```

Headers:

```http
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

Role Required:

```text
ADMIN
```

---

# 🏗 Current Architecture

```text
                              Client
                                │
                                ▼
                        JWT Authentication
                                │
                                ▼
                         Spring Security
                                │
                                ▼
                         REST Controllers
                                │
         ┌──────────────────────┼──────────────────────┐
         │                      │                      │
         ▼                      ▼                      ▼
   Auth Service          Order Service         Payment Service
                                                        │
                         ┌──────────────────────────────┼──────────────────────┐
                         ▼                              ▼                      ▼
                 Webhook Service              Notification Service       Admin Service
                         │                              │
                         ▼                              ▼
                  Email Service                   Audit Service
                         │                              │
                         └──────────────┬───────────────┘
                                        ▼
                                  Repository Layer
                                        │
                                        ▼
                                  PostgreSQL
```

---

# 🐳 Docker Architecture

The application is containerized as:

```text
                         Docker Compose
                               │
                    ┌──────────┴──────────┐
                    │                     │
                    ▼                     ▼
             ┌─────────────┐       ┌───────────────┐
             │  dpgs-app   │       │ dpgs-postgres │
             │             │       │               │
             │ Spring Boot │──────►│ PostgreSQL 16 │
             │ Java 21     │       │               │
             │ Port 8080   │       │ Port 5432     │
             └─────────────┘       └───────┬───────┘
                                           │
                                           ▼
                                    postgres_data
                                       Volume
```

---

# 🔄 Payment Flow

```text
Client
   │
   ▼
Create Order
   │
   ▼
Initiate Payment
   │
   ▼
Payment Pending
   │
   ├──────────────► Webhook Success
   │                    │
   │                    ▼
   │              Payment SUCCESS
   │                    │
   │                    ▼
   │                 Order PAID
   │                    │
   │                    ▼
   │          Notification Created
   │                    │
   │                    ▼
   │             Email Generated
   │                    │
   │                    ▼
   │              Audit Created
   │
   ├──────────────► Webhook Failed
   │                    │
   │                    ▼
   │             Payment FAILED
   │                    │
   │                    ▼
   │               Order FAILED
   │                    │
   │                    ▼
   │               Audit Created
   │
   └──────────────► Scheduler Timeout
                         │
                         ▼
                 Payment EXPIRED
                         │
                         ▼
                  Order FAILED
                         │
                         ▼
                   Audit Created
```

---

# 🗄 Database

## PostgreSQL

The project uses PostgreSQL 16.

### Local Development

When running PostgreSQL directly on the host machine:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dpgs
spring.datasource.username=postgres
spring.datasource.password=********
```

### Docker Environment

When running with Docker Compose:

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/dpgs
spring.datasource.username=postgres
spring.datasource.password=admin
```

The Docker configuration uses the Compose service name:

```text
postgres
```

---

# ▶ Running the Project

There are two ways to run the project.

---

## Option 1 — Docker Compose (Recommended)

Make sure Docker Desktop is running.

Clone the repository:

```bash
git clone https://github.com/Balmik517/digital-payment-gateway-simulator.git
```

Move to the project directory:

```bash
cd digital-payment-gateway-simulator
```

Build and start all services:

```bash
docker compose up -d --build
```

Check the running services:

```bash
docker compose ps
```

Expected services:

```text
dpgs-app
dpgs-postgres
```

Application:

```text
http://localhost:8080
```

PostgreSQL:

```text
localhost:5432
```

Stop the application:

```bash
docker compose down
```

---

## Option 2 — Run Locally with Maven

Make sure Java 21, Maven, and PostgreSQL are installed.

Build the project:

```bash
mvn clean install -DskipTests
```

Run the application:

```bash
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

---

# 🔧 Useful Docker Commands

### Check Docker version

```bash
docker --version
```

### List Docker images

```bash
docker images
```

### List running containers

```bash
docker ps
```

### List all containers

```bash
docker ps -a
```

### Build DPGS image manually

```bash
docker build -t dpgs-app .
```

### Run DPGS container manually

```bash
docker run -p 8080:8080 dpgs-app
```

### Check Docker Compose services

```bash
docker compose ps
```

### Start services

```bash
docker compose up -d
```

### Build and start services

```bash
docker compose up -d --build
```

### Stop services

```bash
docker compose down
```

### Connect to PostgreSQL container

```bash
docker exec -it dpgs-postgres psql -U postgres -d dpgs
```

Inside PostgreSQL:

```sql
\dt
```

Check registered users:

```sql
SELECT * FROM users;
```

---

# 🧪 Docker Validation

The Docker setup has been validated with the following scenarios:

```text
✅ Spring Boot application starts successfully
✅ PostgreSQL 16 starts successfully
✅ Spring Boot connects to PostgreSQL container
✅ User registration works
✅ User login works
✅ Order APIs work
✅ Payment APIs work
✅ PostgreSQL data persists using Docker volume
✅ docker compose down → up works
✅ docker compose up -d --build works
```

---

# 📌 Implemented Modules

* ✅ Authentication
* ✅ JWT Security
* ✅ Role Based Access Control (RBAC)
* ✅ Order Management
* ✅ Payment Management
* ✅ Payment Webhooks
* ✅ Payment Expiry Scheduler
* ✅ Notification Service
* ✅ Mock Email Service
* ✅ Audit Service
* ✅ Admin Module
* ✅ Request Logging
* ✅ Exception Handling
* ✅ Ownership Validation
* ✅ Docker Containerization
* ✅ Docker Compose
* ✅ PostgreSQL Containerization
* ✅ PostgreSQL Persistent Volume

---

# 🛣 Roadmap

## Phase 1 ✅ — Core Backend

* JWT Authentication
* Order Service
* Payment Service
* Notification Service
* Audit Trail
* Logging
* Exception Handling
* Ownership Validation

---

## Phase 2 — Payment Improvements

* Payment Retry
* Refund APIs
* Validation Improvements

---

## Phase 3 — Event-Driven Architecture

* Kafka Integration
* Event-Driven Architecture
* Async Notification Processing

---

## Phase 4 ✅ — Containerization

* Docker
* Docker Compose
* Spring Boot Containerization
* PostgreSQL Containerization
* Docker Networking
* Persistent PostgreSQL Volume

---

## Phase 5 — Microservices Architecture

* Microservices
* API Gateway
* Config Server
* Service Discovery
* Independent Service Deployment

---

## Phase 6 — CI/CD & Cloud

* GitHub Actions CI/CD
* Kubernetes
* AWS Deployment
* Monitoring
* Distributed Tracing

---

# 🎯 Learning Objectives

This project demonstrates:

* Enterprise Java Development
* Java 21
* Spring Boot
* Spring Security
* JWT Authentication
* Role-Based Access Control
* REST API Design
* Layered Architecture
* JPA & Hibernate
* PostgreSQL Integration
* Docker
* Docker Compose
* Docker Networking
* Docker Volumes
* Exception Handling
* Audit Logging
* Request Logging
* Secure API Design
* Webhook Processing
* Scheduler Jobs
* Git Feature Branch Workflow
* Containerized Application Deployment
* Production-Inspired Backend Development

---

# 💡 Key Backend Concepts Demonstrated

The project demonstrates practical implementation of:

```text
Authentication
       ↓
Authorization
       ↓
Order Management
       ↓
Payment Processing
       ↓
Webhook Processing
       ↓
Notification
       ↓
Mock Email
       ↓
Audit Logging
       ↓
Scheduled Processing
       ↓
PostgreSQL
       ↓
Docker
       ↓
Docker Compose
```

---

# 👨‍💻 Author

**Balmik Prajapati**

Senior Software Engineer

Java Backend Developer | Spring Boot | Microservices | AWS