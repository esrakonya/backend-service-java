# Distributed Marketplace Backend

The primary goal of this repository is to demonstrate how to handle common challenges in distributed systems—specifically focusing on **data consistency**, **asynchronous messaging**, and **infrastructure automation**. It started as a monolith and was intentionally refactored into microservices to explore patterns like Event-Driven Architecture and Container Orchestration.

## System Architecture

The project is structured as a **Maven Multi-Module** system, consisting of 6 specialized modules and a shared kernel:

*   **API Gateway:** Reactive entry point using **Spring Cloud Gateway**. Handles centralized routing, rate limiting, and global security filters.
*   **Auth Service:** The IAM (Identity & Access Management) hub. Manages RBAC (Role-Based Access Control) using stateless **JWT** and **BCrypt** hashing.
*   **Product Service:** Managed product catalog featuring **Redis Caching** (Cache-Aside pattern) and Kafka event publishing.
*   **Inventory Service:** A critical consistency layer. Implements **Pessimistic Locking** (`SELECT FOR UPDATE`) to prevent overselling during high-concurrency bursts.
*   **Notification Service:** An asynchronous Kafka consumer that provides real-time alerting without blocking core business flows.
*   **Marketplace Common:** The **Shared Kernel** (Core, Web, Persistence). Centralizes DTOs, Event Contracts, and Global Exception Handling to ensure architectural DRYness.

---

## The Engineering Deep Dive

### 1. Data Integrity & Concurrency
In a distributed environment, "race conditions" are the enemy. I implemented a strict concurrency control mechanism in the **Inventory Service**. By using JPA Pessimistic Locking, the system ensures that stock updates are atomic at the database level, verified through multi-threaded stress tests.

### 2. Event-Driven Reliability (Apache Kafka)
Service communication is decoupled using Kafka. To ensure no data is lost during transit:
*   **Non-blocking Retries:** Implemented with exponential backoff.
*   **Dead Letter Topics (DLT):** Failed messages are routed for manual inspection.
*   **Transactional Messaging:** Utilized `TransactionSynchronizationManager` to ensure Kafka events are only dispatched *after* the database commit (Atomic Commit pattern).

### 3. Full-Stack Observability
"If you can't measure it, you can't manage it." The system features a production-grade observability stack:
*   **Distributed Tracing:** Integrated **Zipkin** with **B3 Propagation**. A single request can be tracked across the Gateway, Kafka, and multiple services via a unique **Trace ID**.
*   **Centralized Logging:** All 14 containers stream structured JSON logs to **Logstash**, which are indexed in **Elasticsearch** and visualized in **Kibana (ELK Stack)**.
*   **Metrics:** **Prometheus** scrapes metrics from Spring Actuator endpoints, visualized in custom **Grafana** dashboards.

---

## DevOps & Cloud Native Infrastructure

The entire fleet is orchestrated using **Kubernetes** and automated via a modern **CI/CD** pipeline.

*   **Orchestration:** Managed by **Helm Charts**. All deployments are templated and parameterized via `values.yaml`.
*   **Infrastructure as Code:** Local K8s cluster (Docker Desktop) running 14 stable pods.
*   **Automated Secrets:** Sensitive data (Postgres passwords, JWT keys) are managed via **GitHub Secrets** and injected into **K8s Secrets** at runtime—ensuring zero sensitive data in the repository.
*   **Optimization:** Dockerfiles are multi-stage and optimized for **ARM64 (Mac M3)** architecture, running as **non-root** users for enhanced security.

---

## Technical Challenges Solved

*   **Circular Dependency Resolution:** Solved the Auth/Common module loop by abstracting security contracts into the Shared Kernel.
*   **Gateway Synchronization:** Fixed YAML override issues to aggregate Swagger documentation from all services into a single UI.
*   **Multi-Module Component Scanning:** Implemented `scanBasePackages` and `EntityScan` strategies to allow Spring to discover components across different JAR modules.
*   **Native DNS Fixes:** Resolved Netty resolver issues specific to Apple Silicon during cloud-native development.

---

## Getting Started

### Requirements
*   Java 17 / Maven 3.9
*   Docker Desktop (K8s Enabled)
*   Helm 3.x

### Deployment
1. **Clone the Repo:** `git clone https://github.com/esrakonya/backend-service-java.git`
2. **Build JARs:** `mvn clean package -DskipTests`
3. **Deploy Infrastructure:** `helm upgrade --install marketplace ./helm-charts/marketplace`
4. **Access APIs:** Gateway is exposed at `http://localhost:8080/swagger-ui.html`

---
**Author:** [Esra Konya](https://github.com/esrakonya)  
*Java Backend Engineer focused on Distributed Systems.*
