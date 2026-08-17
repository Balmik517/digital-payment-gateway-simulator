# Digital Payment Gateway Simulator

A production-inspired backend application built using **Java 21**, **Spring Boot 3.5.x**, **Spring Security**, **JWT Authentication**, and **PostgreSQL** that simulates a real-world Digital Payment Gateway.

The project follows enterprise backend development practices including secure authentication, authorization, role-based access control, order management, payment processing, webhook simulation, scheduled jobs, notifications, audit logging, request logging, exception handling, and Git feature-based development.

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
- Spring Scheduler
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
|-----------|--------|--------|
| /api/auth/** | ✅ | ✅ |
| /api/orders/** | ✅ | ✅ |
| /api/payments/** | ✅ | ✅ |
| /api/notifications/** | ✅ | ✅ |
| /api/admin/** | ❌ | ✅ |

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
```

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

### Payment Features

- Payment Idempotency
- Payment Ownership Validation
- Payment State Validation
- Payment Expiry Scheduler
- Webhook Payment Processing
- Audit Tracking

---

## 🔄 Webhook Simulation

Simulates payment gateway callbacks similar to Razorpay, Stripe, or Cashfree.

### APIs

```http
POST /api/webhooks/payment-success
POST /api/webhooks/payment-failed
```

### Features

- Processes asynchronous payment callbacks
- Updates Payment Status
- Updates Order Status
- Generates Notifications
- Creates Audit Records
- Prevents Duplicate Processing

---

## ⏰ Scheduled Jobs

Implemented automatic payment expiration using Spring Scheduler.

### Current Job

- Expires pending payments after configured timeout
- Updates Payment Status
- Updates Order Status
- Creates Audit Records

---

## 🔔 Notification Module

- Notification Entity
- Notification Service
- Notification Repository
- Get Logged-in User Notifications
- Automatic Notification Creation on Successful Payment

---

## 📧 Mock Email Service

A mock email service is implemented to simulate real-world email delivery.

### Features

- Payment Success Email Simulation
- Structured Email Logs
- Notification Integration
- Audit Tracking

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

Tracks every important business event.

### Implemented Audit Events

- PAYMENT_CREATED
- PAYMENT_PENDING
- PAYMENT_SUCCESS
- PAYMENT_FAILED
- PAYMENT_EXPIRED
- NOTIFICATION_SENT
- EMAIL_SENT

---

## 👨‍💼 Admin Module

Administrative APIs protected using ROLE_ADMIN.

### Implemented APIs

#### Get All Payments

```http
GET /api/admin/payments
```

### Features

- Admin-only access
- View all payments
- System-wide payment visibility
- Payment monitoring

---

## 📝 Logging

Application-wide logging using **SLF4J + Logback**.

### Logging Implemented

- Authentication Logs
- Order Logs
- Payment Logs
- Notification Logs
- Email Logs
- Webhook Logs
- Scheduler Logs
- Request Logs
- Error Logs
- Unauthorized Access Logs

---

## 🛡 Security

- Spring Security Filter Chain
- JWT Authentication
- Role-Based Access Control (RBAC)
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

## Webhooks

### Payment Success Callback

```http
POST /api/webhooks/payment-success
```

Request

```json
{
  "paymentId": "PAY-1785046301509"
}
```

---

### Payment Failed Callback

```http
POST /api/webhooks/payment-failed
```

Request

```json
{
  "paymentId": "PAY-1785046301509"
}
```

---

## Admin APIs

### Get All Payments

```http
GET /api/admin/payments
```

Headers

```http
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

Role Required

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
      ┌─────────────────────┼─────────────────────┐
      │                     │                     │
      ▼                     ▼                     ▼
 Auth Service       Order Service       Payment Service
                                                  │
                          ┌───────────────────────┼──────────────────────┐
                          ▼                       ▼                      ▼
                  Webhook Service      Notification Service      Admin Service
                          │
                          ▼
                    Email Service
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
   │               Order PAID
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
   │              Order FAILED
   │                    │
   │                    ▼
   │              Audit Created
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

PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dpgs
spring.datasource.username=postgres
spring.datasource.password=********
```

---

# ▶ Running the Project

Clone repository

```bash
git clone https://github.com/Balmik517/digital-payment-gateway-simulator.git
```

Move to project directory

```bash
cd digital-payment-gateway-simulator
```

Run application

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
- ✅ Role Based Access Control (RBAC)
- ✅ Order Management
- ✅ Payment Management
- ✅ Payment Webhooks
- ✅ Payment Expiry Scheduler
- ✅ Notification Service
- ✅ Mock Email Service
- ✅ Audit Service
- ✅ Admin Module
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

- Payment Retry
- Refund APIs
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
- Role-Based Access Control
- REST API Design
- Layered Architecture
- JPA & Hibernate
- PostgreSQL Integration
- Exception Handling
- Audit Logging
- Request Logging
- Secure API Design
- Webhook Processing
- Scheduler Jobs
- Git Feature Branch Workflow
- Production-ready Backend Development

---

# 👨‍💻 Author

**Balmik Prajapati**

Senior Software Engineer

Java Backend Developer | Spring Boot | Microservices | AWS