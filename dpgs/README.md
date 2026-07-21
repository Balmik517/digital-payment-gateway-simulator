# Digital Payment Gateway Simulator

A production-inspired backend application built using **Java 21**, **Spring Boot 3.5.x**, **Spring Security**, **JWT Authentication**, and **PostgreSQL** that simulates a real-world Digital Payment Gateway.

The project follows enterprise backend development practices including secure authentication, authorization, order management, payment processing, notifications, audit logging, request logging, exception handling, and Git feature-based development.

---

# 🚀 Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Security
- JWT Authentication
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- Lombok
- SLF4J + Logback
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

## 📦 Order Management

- Create Order
- Get Order By Order ID
- Get Logged-in User Orders
- Order Ownership Validation
- Order Status Tracking

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

- Initiate Payment
- Mark Payment Success
- Mark Payment Failed
- Get Payment Details
- Get Payments By Order

### Payment Validations

- Duplicate Payment Prevention
- Payment Ownership Validation
- Payment State Validation
- Prevent Re-processing of Completed Payments

---

## 🔔 Notification Module

- Notification Entity
- Notification Service
- Notification Repository
- Get Logged-in User Notifications
- Automatic Notification Creation on Successful Payment

---

## 📋 Audit Module

Tracks every important business event.

### Implemented Audit Events

- PAYMENT_CREATED
- PAYMENT_PENDING
- PAYMENT_SUCCESS
- PAYMENT_FAILED
- NOTIFICATION_SENT

---

## 📝 Logging

Application-wide logging using **SLF4J + Logback**.

### Logging Implemented

- Authentication Logs
- Order Logs
- Payment Logs
- Notification Logs
- Request Logs
- Error Logs
- Unauthorized Access Logs

---

## 🛡 Security

- Spring Security Filter Chain
- JWT Authentication
- Resource Ownership Validation
- Stateless Authentication
- Global Exception Handling
- Custom Exceptions

---

# 📂 Project Structure

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

Request

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

Request

```json
{
  "email": "balmik@gmail.com",
  "password": "password123"
}
```

Response

```json
{
  "token": "JWT_TOKEN"
}
```

---

## Orders

### Create Order

```http
POST /api/orders
```

Headers

```http
Authorization: Bearer <JWT_TOKEN>
```

Request

```json
{
  "amount": 5000,
  "description": "Laptop Purchase"
}
```

---

### Get Order By ID

```http
GET /api/orders/{orderId}
```

---

### Get My Orders

```http
GET /api/orders/my-orders
```

---

## Payments

### Initiate Payment

```http
POST /api/payments/initiatePayment
```

Request

```json
{
  "orderId": "ORD-1783358727708",
  "paymentMethod": "UPI"
}
```

---

### Mark Payment Success

```http
POST /api/payments/{paymentId}/success
```

---

### Mark Payment Failed

```http
POST /api/payments/{paymentId}/fail
```

---

### Get Payment

```http
GET /api/payments/getPayment/{paymentId}
```

---

### Get Payments By Order

```http
GET /api/payments/order/{orderId}
```

---

## Notifications

### Get Logged-in User Notifications

```http
GET /api/notifications/my-notifications
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
                        ▼
                 Service Layer
      ┌─────────────┼─────────────┐
      │             │             │
      ▼             ▼             ▼
 Order Service  Payment Service  Notification Service
                      │
                      ▼
                 Audit Service
                      │
                      ▼
               Repository Layer
                      │
                      ▼
                  PostgreSQL
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
Order Created
   │
   ▼
Initiate Payment
   │
   ▼
Payment Pending
   │
   ├──────────────► Payment Success
   │                    │
   │                    ▼
   │              Order Paid
   │                    │
   │                    ▼
   │          Notification Created
   │                    │
   │                    ▼
   │            Audit Record Saved
   │
   └──────────────► Payment Failed
                        │
                        ▼
                  Order Failed
                        │
                        ▼
                 Audit Record Saved
```

---

# 🔐 Security Flow

```text
Client

   │
   ▼

Login

   │
   ▼

JWT Token

   │
   ▼

Authorization Header

Bearer <JWT>

   │
   ▼

JWT Authentication Filter

   │
   ▼

Spring Security

   │
   ▼

Controller

   │
   ▼

Service

   │
   ▼

Database
```

---

# 🗄 Database

PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dpgs
spring.datasource.username=postgres
spring.datasource.password=********
```

---

# ▶ Running the Project

Clone the repository

```bash
git clone https://github.com/Balmik517/digital-payment-gateway-simulator.git
```

Move to the project directory

```bash
cd digital-payment-gateway-simulator
```

Run the application

```bash
mvn spring-boot:run
```

Application URL

```text
http://localhost:8080
```

---

# 📌 Implemented Modules

- ✅ Authentication
- ✅ JWT Security
- ✅ Order Management
- ✅ Payment Management
- ✅ Notification Service
- ✅ Audit Service
- ✅ Request Logging
- ✅ Exception Handling
- ✅ Ownership Validation

---

# 🛣 Roadmap

## Phase 1 ✅

- JWT Authentication
- Order Service
- Payment Service
- Notification Service
- Audit Trail
- Logging

---

## Phase 2

- Email Service (Mock)
- Payment Retry
- Refund APIs
- Admin APIs
- Validation Improvements

---

## Phase 3

- Kafka Integration
- Event-Driven Architecture
- Async Notification Processing

---

## Phase 4

- Docker
- Docker Compose
- Redis Cache

---

## Phase 5

- Microservices
- API Gateway
- Config Server
- Service Discovery

---

## Phase 6

- GitHub Actions CI/CD
- Kubernetes
- AWS Deployment
- Monitoring
- Distributed Tracing

---

# 🎯 Learning Objectives

This project demonstrates:

- Enterprise Java Development
- Spring Boot
- Spring Security
- JWT Authentication
- REST API Design
- Layered Architecture
- JPA & Hibernate
- PostgreSQL Integration
- Exception Handling
- Audit Logging
- Request Logging
- Secure API Design
- Git Feature Branch Workflow
- Production-ready Backend Development

---

# 👨‍💻 Author

**Balmik Prajapati**

Senior Software Engineer

Java Backend Developer | Spring Boot | Microservices | AWS