# Digital Payment Gateway Simulator

A backend application built using Java, Spring Boot, Spring Security, JWT, PostgreSQL, and Maven that simulates a digital payment gateway with secure authentication, order management, and payment lifecycle processing.

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- Git & GitHub

---

## Features

### Authentication

- User Registration
- User Login
- BCrypt Password Encryption
- JWT Token Generation
- JWT Authentication Filter
- Protected APIs
- Stateless Authentication

### Order Management

- Create Order
- Get Order By Order ID
- Get Logged-In User Orders
- Order Ownership Validation
- Order Status Management

### Security

- Spring Security Filter Chain
- JWT Bearer Token Authentication
- Role-Based Foundation
- Global Exception Handling

---

## Project Structure

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
├── repository
├── security
├── service
│   └── impl
└── DpgsApplication
```

---

## Authentication APIs

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
  "accessToken": "jwt-token",
  "tokenType": "Bearer"
}
```

---

## Order APIs

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

Response

```json
{
  "orderId": "ORD-1751812345678",
  "amount": 5000,
  "description": "Laptop Purchase",
  "status": "CREATED"
}
```

---

### Get Order By Order ID

```http
GET /api/orders/{orderId}
```

Headers

```http
Authorization: Bearer <JWT_TOKEN>
```

---

### Get My Orders

```http
GET /api/orders/my-orders
```

Headers

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## Order Lifecycle

```text
CREATED
    |
PAYMENT_PENDING
    |
PAID
```

or

```text
CREATED
    |
PAYMENT_PENDING
    |
FAILED
```

---

## Database

PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dpgs
spring.datasource.username=postgres
spring.datasource.password=******
```

---

## Run Locally

Clone repository

```bash
git clone https://github.com/Balmik517/digital-payment-gateway-simulator.git
```

Move to project

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

## Upcoming Features

- Payment Service
- UPI Payment Simulation
- Card Payment Simulation
- Payment Status Tracking
- Notification Service
- Kafka Integration
- Dockerization
- AWS Deployment
- GitHub Actions CI/CD
- Microservices Architecture

---

## Learning Objectives

This project demonstrates:

- Spring Boot Development
- REST API Design
- JWT Authentication
- Spring Security
- JPA & Hibernate
- PostgreSQL Integration
- Exception Handling
- Git Branching Strategy
- CI/CD Readiness
- Cloud Deployment Preparation

---

## Author

**Balmik Prajapati**

Java Backend Developer | Spring Boot | Microservices | AWS