# Java Backend Service

A professional-grade backend service built to demonstrate core software engineering principles, environment-based configuration, and structured logging.

## Tech Stack
- **Language:** Java 17
  - **Framework:** Spring Boot 3.2.5
  - **Build Tool:** Maven
  - **Logging:** SLF4J (API) + Logback (implementation)
  - **Database:** PostgreSQL 16 (Alpine)
  - **ORM:** Hibernate / Spring Data JPA
  - **Connection Pool:** HikariCP
  - **Infrastructure:** Docker & Docker Compose
  - **Version Control:** Git

## Project Status
  
### Phase 1: Core Infrastructure (Completed)
- Integrated **Spring Boot 3.2.5** for enterprise-level capabilities.
- Configured **SLF4J/Logback** for structured and environment-aware logging.
- Established a clean **Maven** build lifecycle with Java 17 compatibility.

### Phase 2: Persistence Layer (In Progress)
- Implemented **Docker-based PostgreSQL** for consistent local development.
- Integrated **Spring Data JPA** for robust Data Access Object (DAO) patterns.
- Configured **HikariCP** for high-performance connection pooling.
- Established **Entity-Repository mapping** for the Marketplace domain.

## Getting Started

### Prerequisites
- JDK 17
- Maven 3.x
- Docker Desktop

### Infrastructure Setup
To start the database:
```bash
docker compose up -d
```

### How to Run
To run the application locally, use the following command:
```bash
mvn clean spring-boot:run
```