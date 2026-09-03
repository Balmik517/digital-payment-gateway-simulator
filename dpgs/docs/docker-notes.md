# Docker Notes — Digital Payment Gateway Simulator

## 1. What is Docker?

Docker is a platform used to **package an application together with everything it needs to run**.

Think of Docker as a **portable application box**.

For example, the DPGS application needs:

```text
Java 21
Spring Boot
Application JAR
PostgreSQL Driver
Configuration
Environment Variables
```

Without Docker, another developer may need to install and configure these things manually.

With Docker:

```text
DPGS Application
+
Java Runtime
+
Dependencies
+
Configuration
        ↓
   Docker Image
        ↓
   Docker Container
```

The application can then run consistently across different environments.

---

# 2. The "Works on My Machine" Problem

This is one of the main problems Docker helps solve.

### Your machine

```text
Windows 11
Java 21
Maven
PostgreSQL 16
```

The application works.

### Another developer's machine

```text
Windows 10
Java 17
Maven
PostgreSQL 14
```

The application may fail because of:

```text
Java version differences
Database version differences
Configuration differences
Dependency differences
Environment differences
```

Then comes the classic statement:

```text
"But it works on my machine!"
```

Docker helps standardize the runtime environment.

---

# 3. How Docker Solves This

Without Docker, you may have to provide instructions like:

```text
Install Java
Install Maven
Install PostgreSQL
Create database
Configure username/password
Configure application properties
Start PostgreSQL
Start Spring Boot application
```

With Docker, the environment can be packaged and started using Docker commands.

For DPGS, Docker Compose can start:

```text
Spring Boot Application
        +
PostgreSQL
```

with:

```bash
docker compose up -d --build
```

---

# 4. Docker Terminology

The four most important concepts are:

```text
Image
Container
Dockerfile
Docker Compose
```

---

# 5. Docker Image

A Docker Image is a **blueprint/template** used to create containers.

A useful Java analogy is:

```text
Java Class → Blueprint
Object     → Instance
```

Similarly:

```text
Docker Image → Blueprint
Container    → Running Instance
```

Examples of Docker images:

```text
postgres:16
redis:latest
nginx
eclipse-temurin:21-jdk
```

An image itself is not the running application.

---

# 6. Docker Container

A Container is a **running instance of a Docker Image**.

For example:

```bash
docker run postgres
```

Docker will:

```text
Find PostgreSQL Image
        ↓
Create Container
        ↓
Start PostgreSQL
```

For your project, you have:

```text
dpgs-app
```

and:

```text
dpgs-postgres
```

running as containers.

---

# 7. Image vs Container

Remember this clearly:

```text
IMAGE
  ↓
Blueprint

CONTAINER
  ↓
Running instance
```

Java analogy:

```text
Class
  ↓
Object
```

Docker analogy:

```text
Image
  ↓
Container
```

---

# 8. Dockerfile

A Dockerfile is a **recipe containing instructions used to build a Docker Image**.

It tells Docker:

```text
What base image to use
Where to place the application
Which files to copy
Which ports the application uses
How to start the application
```

---

# 9. DPGS Dockerfile

Your actual Dockerfile is:

```dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/dpgs-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

# 10. Dockerfile Instructions

## FROM

```dockerfile
FROM eclipse-temurin:21-jdk
```

Defines the **base image**.

In your project:

```text
Java 21
```

is provided by the Eclipse Temurin image.

Think:

```text
FROM = Starting point
```

---

## WORKDIR

```dockerfile
WORKDIR /app
```

Sets the working directory inside the container.

After this instruction:

```text
/app
```

becomes the working directory.

Think:

```text
WORKDIR = Where commands/files operate inside the container
```

---

## COPY

```dockerfile
COPY target/dpgs-0.0.1-SNAPSHOT.jar app.jar
```

Copies the Spring Boot JAR from the local machine into the Docker image.

Local:

```text
target/dpgs-0.0.1-SNAPSHOT.jar
```

Container:

```text
/app/app.jar
```

Think:

```text
COPY = Move files into the image
```

---

## EXPOSE

```dockerfile
EXPOSE 8080
```

Documents that the application uses port:

```text
8080
```

Important:

`EXPOSE` itself does **not publish the port to your host machine**.

The port mapping is done when running the container.

For example:

```bash
docker run -p 8080:8080 dpgs-app
```

---

## ENTRYPOINT

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Defines the command that starts the application when the container starts.

Equivalent command:

```bash
java -jar app.jar
```

Think:

```text
ENTRYPOINT = What should start when the container starts?
```

---

# 11. RUN

Another commonly used Dockerfile instruction is:

```dockerfile
RUN
```

Example:

```dockerfile
RUN mkdir /app/logs
```

`RUN` executes a command **while the Docker image is being built**.

Important distinction:

```text
RUN
→ Build time

ENTRYPOINT
→ Container startup
```

---

# 12. CMD

`CMD` provides a default command or arguments for the container.

Example:

```dockerfile
CMD ["java", "-jar", "app.jar"]
```

Basic difference:

```text
RUN
→ Executes during image build

CMD
→ Default startup command

ENTRYPOINT
→ Defines the main startup process
```

For your DPGS application, you are currently using:

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

# 13. Dockerfile Lifecycle

This flow is extremely important:

```text
Dockerfile
     ↓
docker build
     ↓
Docker Image
     ↓
docker run
     ↓
Docker Container
```

Remember:

```text
Dockerfile → Image → Container
```

---

# 14. Docker Compose

Suppose your application needs:

```text
Spring Boot
PostgreSQL
```

You could manually start both:

```bash
docker run postgres
docker run dpgs-app
```

But managing multiple containers manually becomes inconvenient.

Docker Compose allows you to define multiple services in a single YAML file.

For example:

```yaml
services:

  postgres:
    image: postgres:16

  dpgs:
    build: .
```

Then you can start everything with:

```bash
docker compose up
```

For your project:

```bash
docker compose up -d --build
```

---

# 15. Dockerfile vs Docker Compose

This is a common interview question.

### Dockerfile

Used to define **how one application image is built**.

```text
Dockerfile
     ↓
Docker Image
```

### Docker Compose

Used to define and manage **multiple containers/services**.

```text
docker-compose.yml
        ↓
Docker Compose
        ↓
App Container + Database Container
```

Easy way to remember:

```text
Dockerfile
→ Build an image

Docker Compose
→ Run/manage multiple services together
```

---

# 16. DPGS Without Docker

Without Docker, your environment would look like:

```text
Your Computer

 ├── Java 21
 ├── Maven
 ├── PostgreSQL
 └── Spring Boot Application
```

Everything needs to be configured locally.

---

# 17. DPGS With Docker

Your current setup is:

```text
Docker Compose
      │
      ├── dpgs-app
      │      └── Spring Boot + Java 21
      │
      └── dpgs-postgres
             └── PostgreSQL 16
```

The application and database run in separate containers.

---

# 18. DPGS Docker Architecture

Your current architecture can be visualized as:

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

# 19. Docker Networking in DPGS

This is an important part of your implementation.

Inside Docker Compose, the application connects to PostgreSQL using the **service name**.

Your Docker configuration uses:

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/dpgs
```

Here:

```text
postgres
```

is the Docker Compose service name.

It is not:

```text
localhost
```

from inside the application container.

The communication is:

```text
dpgs-app
   │
   │ Docker Network
   ▼
postgres:5432
   │
   ▼
PostgreSQL
```

This is a very important interview concept.

---

# 20. Why Not `localhost`?

Inside the Spring Boot container:

```text
localhost
```

means:

```text
the Spring Boot container itself
```

It does **not** mean the PostgreSQL container.

Therefore:

```properties
jdbc:postgresql://localhost:5432/dpgs
```

would attempt to find PostgreSQL inside the application container.

Instead:

```properties
jdbc:postgresql://postgres:5432/dpgs
```

means:

```text
Connect to the PostgreSQL service named "postgres"
```

---

# 21. Docker Volume

Containers are temporary by nature.

If the PostgreSQL container is removed, we don't want to lose database data.

Therefore, Docker volumes are used for persistent data.

Your PostgreSQL configuration uses:

```text
postgres_data
```

Conceptually:

```text
PostgreSQL Container
        │
        ▼
postgres_data Volume
        │
        ▼
Persistent Database Data
```

This means the PostgreSQL data can survive container recreation.

---

# 22. Docker Compose Persistence Test

You successfully tested:

```bash
docker compose down
```

and then:

```bash
docker compose up -d
```

Your PostgreSQL data remained available.

That confirms your named volume is working.

Important:

```bash
docker compose down
```

removes containers and the network, but keeps named volumes.

Whereas:

```bash
docker compose down -v
```

also removes the volumes.

Therefore:

```text
docker compose down
        ↓
Data preserved

docker compose down -v
        ↓
Volume deleted
        ↓
Database data deleted
```

---

# 23. Docker Architecture

At a high level:

```text
Docker Client
      │
      ▼
Docker Engine
      │
 ┌────┴────────┐
 │             │
 ▼             ▼
Images      Containers
```

---

# 24. Docker Client

The Docker commands that you type are part of the Docker client.

Examples:

```bash
docker ps
docker images
docker run
docker build
docker stop
```

The Docker client communicates with the Docker Engine.

---

# 25. Docker Engine

Docker Engine is the component that actually manages:

```text
Images
Containers
Networks
Volumes
```

For example:

```bash
docker run postgres
```

The Docker Engine performs the required container operations.

---

# 26. Docker Images

To list images:

```bash
docker images
```

Example:

```text
postgres
eclipse-temurin
dpgs-app
```

Think:

```text
Images = Blueprints
```

---

# 27. Docker Containers

To list currently running containers:

```bash
docker ps
```

Example:

```text
dpgs-app
dpgs-postgres
```

Think:

```text
Containers = Running applications
```

---

# 28. List All Containers

```bash
docker ps -a
```

This shows:

```text
Running containers
Stopped containers
Exited containers
```

---

# 29. Docker Commands

## Check Docker Version

```bash
docker --version
```

Example:

```text
Docker version 28.x.x
```

---

## Check Images

```bash
docker images
```

---

## Check Running Containers

```bash
docker ps
```

---

## Check All Containers

```bash
docker ps -a
```

---

## Build Image

```bash
docker build -t dpgs-app .
```

Meaning:

```text
docker build
    ↓
Build image using Dockerfile

-t dpgs-app
    ↓
Give image the name dpgs-app

.
    ↓
Use current directory as build context
```

---

# 30. Run Container Manually

```bash
docker run -p 8080:8080 dpgs-app
```

The mapping:

```text
8080:8080
   │    │
   │    └── Container port
   └─────── Host port
```

So:

```text
localhost:8080
        ↓
Container:8080
```

---

# 31. Stop Container

```bash
docker stop <container-id>
```

Example:

```bash
docker stop dpgs-app
```

This stops the running container.

---

# 32. Remove Container

```bash
docker rm <container-id>
```

This removes the container.

Removing a container does not automatically mean the Docker image is removed.

---

# 33. Remove Image

```bash
docker rmi <image-id>
```

This removes the Docker image.

Remember:

```text
docker stop
→ Stop container

docker rm
→ Remove container

docker rmi
→ Remove image
```

---

# 34. Maven + Docker Workflow

Your DPGS application follows this workflow:

```text
Source Code
     ↓
Maven Build
     ↓
JAR File
     ↓
Docker Build
     ↓
Docker Image
     ↓
Docker Container
```

For example:

```bash
mvn clean install -DskipTests
```

creates:

```text
target/dpgs-0.0.1-SNAPSHOT.jar
```

Then:

```bash
docker build -t dpgs-app .
```

creates the Docker image.

Then:

```bash
docker run -p 8080:8080 dpgs-app
```

runs the container.

---

# 35. Docker Compose Workflow in DPGS

For your complete project, the flow is:

```text
pom.xml
   ↓
Maven Build
   ↓
Spring Boot JAR
   ↓
Dockerfile
   ↓
DPGS Image
   ↓
docker-compose.yml
   ↓
Docker Compose
   ├───────────────┐
   ▼               ▼
dpgs-app      dpgs-postgres
   │               │
   └──── Network ──┘
                   │
                   ▼
             postgres_data
```

---

# 36. Your Main Docker Compose Command

The most important command for your project is:

```bash
docker compose up -d --build
```

Breakdown:

```text
docker compose
→ Use Docker Compose

up
→ Create/start services

-d
→ Detached/background mode

--build
→ Build/rebuild images before starting
```

---

# 37. Check Compose Services

```bash
docker compose ps
```

Your successful output looks like:

```text
NAME            IMAGE         SERVICE    STATUS
dpgs-app        dpgs-dpgs     dpgs       Up
dpgs-postgres   postgres:16   postgres   Up
```

This confirms both services are running.

---

# 38. Stop Docker Compose

```bash
docker compose down
```

This stops and removes the Compose containers and network.

Your PostgreSQL named volume remains.

---

# 39. Start Again

```bash
docker compose up -d
```

This starts the services again.

---

# 40. Rebuild Everything

When you change the application code:

```bash
docker compose up -d --build
```

This rebuilds the application image and starts the updated containers.

---

# 41. Accessing DPGS

After Compose starts successfully:

```text
Spring Boot:
http://localhost:8080
```

PostgreSQL:

```text
localhost:5432
```

Inside Docker, the application connects using:

```text
postgres:5432
```

---

# 42. Verify PostgreSQL Container

You can connect directly to the PostgreSQL container:

```bash
docker exec -it dpgs-postgres psql -U postgres -d dpgs
```

Then:

```sql
\dt
```

to list tables.

For example:

```sql
SELECT * FROM users;
```

This allows you to verify that the application is actually storing data in the containerized PostgreSQL database.

---

# 43. DPGS End-to-End Docker Flow

The complete flow is:

```text
Developer
    │
    ▼
Maven Build
    │
    ▼
Spring Boot JAR
    │
    ▼
Dockerfile
    │
    ▼
Docker Image
    │
    ▼
Docker Compose
    │
    ├───────────────┐
    ▼               ▼
dpgs-app       dpgs-postgres
    │               │
    │               │
    └──Docker───────┘
       Network
          │
          ▼
    PostgreSQL DB
          │
          ▼
    postgres_data
       Volume
```

---

# 44. What You Have Implemented in DPGS

Your Docker implementation currently includes:

```text
✅ Dockerfile
✅ Java 21 Docker Image
✅ Spring Boot Docker Container
✅ PostgreSQL 16 Container
✅ Docker Compose
✅ Docker Network
✅ PostgreSQL Named Volume
✅ Container-to-container communication
✅ Persistent Database Data
✅ Docker Rebuild
✅ Docker Compose Lifecycle
```

You have also successfully tested:

```bash
docker compose up -d --build
```

and the application starts correctly.

---

# 45. Important Interview Questions

## Q1. What is Docker?

A good answer:

> Docker is a containerization platform that packages an application together with its runtime, dependencies, and configuration into a portable containerized environment. It helps ensure consistent application behavior across development, testing, and production environments.

---

## Q2. What is a Docker Image?

> A Docker Image is a read-only blueprint or template used to create containers.

---

## Q3. What is a Docker Container?

> A container is a running instance of a Docker image.

---

## Q4. What is a Dockerfile?

> A Dockerfile contains instructions used to build a Docker image.

---

## Q5. Difference between Dockerfile and Docker Compose?

> A Dockerfile defines how to build an application image, while Docker Compose defines and manages multiple services or containers that work together.

---

## Q6. Why use Docker Compose?

> Docker Compose simplifies running multi-container applications by defining services, networking, environment variables, ports, and volumes in a single configuration file.

---

## Q7. Why doesn't your Spring Boot container use `localhost` for PostgreSQL?

> Because `localhost` inside the Spring Boot container refers to that container itself. Since PostgreSQL is running in another container, the application connects using the Docker Compose service name, which in my project is `postgres`.

---

## Q8. How do you persist PostgreSQL data?

> I use a Docker named volume, `postgres_data`, mounted to PostgreSQL's data directory. This allows the database data to survive container recreation.

---

## Q9. What happens when you run `docker compose down`?

> Docker Compose stops and removes the containers and network created by Compose. Named volumes are preserved unless `-v` is specified.

---

## Q10. What happens with `docker compose down -v`?

> It also removes the Compose volumes, so persistent PostgreSQL data stored in those volumes is deleted.

---

# 46. Most Important Commands to Remember

### Build Java application

```bash
mvn clean install -DskipTests
```

### Build Docker image

```bash
docker build -t dpgs-app .
```

### List images

```bash
docker images
```

### Run container

```bash
docker run -p 8080:8080 dpgs-app
```

### List running containers

```bash
docker ps
```

### List all containers

```bash
docker ps -a
```

### Stop container

```bash
docker stop <container>
```

### Remove container

```bash
docker rm <container>
```

### Remove image

```bash
docker rmi <image>
```

### Start complete DPGS stack

```bash
docker compose up -d --build
```

### Check Compose services

```bash
docker compose ps
```

### Stop Compose stack

```bash
docker compose down
```

### Restart

```bash
docker compose up -d
```

### Connect to PostgreSQL container

```bash
docker exec -it dpgs-postgres psql -U postgres -d dpgs
```

---

# 47. Final Docker Mental Model

Remember these four concepts:

```text
Dockerfile
    ↓
Instructions
    ↓
Docker Image
    ↓
Template
    ↓
Docker Container
    ↓
Running Application
```

And for multiple services:

```text
Docker Compose
      ↓
Multiple Containers
      ↓
Application + Database
      ↓
Network + Volumes
```

For your DPGS project:

```text
Dockerfile
     ↓
DPGS Image
     ↓
dpgs-app Container
     │
     │ Docker Network
     ▼
dpgs-postgres Container
     │
     ▼
postgres_data Volume
```

### One-line interview summary

> **In my Digital Payment Gateway Simulator, I containerized the Java 21 Spring Boot application using a Dockerfile and used Docker Compose to run the application and PostgreSQL 16 as separate services, connected through a Docker network with persistent PostgreSQL data stored in a named volume.**

This is the version I recommend keeping as your **final Docker study note** for DPGS.
