# Distributed Marketplace (Backend)

This is a microservices-based project built with **Java 17** and **Spring Boot 3.2**. I started this project as a monolith and gradually refactored it into a distributed architecture to learn how to solve common challenges in modern software development, such as service communication, data consistency, and cloud-native deployment.

## System Architecture

The project is structured using **Maven Multi-Module**, consisting of the following services:

*   **Gateway Service:** The entry point for all requests, handling routing and basic fault tolerance with **Resilience4j**.
*   **Auth Service:** Manages user authentication and authorization using **JWT** and **Spring Security 6**.
*   **Product Service:** Handles product listings and uses **Redis** for simple caching.
*   **Inventory Service:** Manages stock levels and ensures data integrity during concurrent updates.
*   **Notification Service:** A separate service that listens to **Kafka** topics to process notifications.
*   **Marketplace Common:** A shared library used by all services for common DTOs, events, and utilities.

## Key Features & Learning Points

### 1. Handling Concurrency
In the **Inventory Service**, I implemented **Pessimistic Locking** (`SELECT FOR UPDATE`) to prevent issues where multiple orders might try to deduct the same stock item simultaneously. This ensures data integrity under high-load scenarios.

### 2. Message-Driven Communication
I integrated **Apache Kafka** to allow services to communicate asynchronously. Key learnings include:
*   **Transactional Events:** Ensuring a message is sent to the broker only after a successful database commit.
*   **Error Handling:** Basic retry mechanisms for consumer resilience.

### 3. Monitoring & Observability
To gain visibility into the distributed system, I integrated:
*   **Zipkin:** To trace request flows from the Gateway through various backend services.
*   **ELK Stack:** Collecting and centralizing logs from all 14 containers for easier debugging and monitoring.

## Infrastructure & DevOps

The project is fully containerized and designed for a **Kubernetes** environment.

*   **Docker & Compose:** Used to manage the development environment with 13+ containers (Services, DBs, Kafka, etc.).
*   **Kubernetes & Helm:** Packaged as a **Helm Chart** to simplify deployment and configuration management on a K8s cluster.
*   **CI/CD:** Automated the build process and Docker image creation using **GitHub Actions**.

## Technical Challenges Solved

*   **Dependency Management:** Structuring the project into 6 modules while maintaining a clean dependency graph without circular references.
*   **Environment Configuration:** Moving sensitive data (like passwords and secrets) out of the code and into **K8s Secrets** and **Environment Variables**.
*   **Service Discovery:** Configuring the API Gateway to correctly route traffic to internal services within the Kubernetes cluster.

## How to Run

### Prerequisites
*   Java 17 & Maven
*   Docker Desktop (with Kubernetes enabled)
*   Helm

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/esrakonya/backend-service-java.git
