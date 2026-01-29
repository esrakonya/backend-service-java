# Secure Distributed Marketplace Backend 

A professional-grade, resilient, and event-driven backend system built with **Spring Boot 3.2** and **Java 17**. This project serves as a comprehensive demonstration of modern distributed system patterns, including asynchronous messaging, distributed caching, and high-concurrency data integrity.

## Key Engineering Accomplishments

### 1. Event-Driven Architecture (EDA) & Resilience
- **Message Broker:** Integrated **Apache Kafka** for decoupled domain communication.
- **Non-blocking Retries:** Implemented a robust retry mechanism using `@RetryableTopic` with **Exponential Backoff** (2s, 4s, 8s...) to handle transient failures.
- **Fault Tolerance:** Automated message routing to **Dead Letter Topics (DLT)** after maximum retries to ensure zero data loss and manual intervention capabilities.
- **Transactional Consistency:** Utilized `TransactionSynchronizationManager` to implement the **Transactional Event Publishing** pattern, ensuring events are only sent to Kafka after a successful database commit.

### 2. Concurrency Control & Data Integrity
- **Strategic Locking:** Implemented **Pessimistic Locking** (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) to manage high-contention inventory updates, preventing "Lost Updates" and overselling.
- **Stress Tested:** Verified logic through a multi-threaded integration suite using `ExecutorService` and `CountDownLatch` to simulate simultaneous purchase bursts.

### 3. Distributed Performance Optimization
- **Caching Layer:** Integrated **Redis** using the **Cache-Aside Pattern**.
- **Consistency:** Implemented `@Cacheable` for lightning-fast reads and `@CacheEvict` (All-Entries strategy) for immediate data consistency upon updates.
- **Resource Management:** Optimized database I/O, reducing PostgreSQL load for frequently accessed metadata.

### 4. Security & Identity Management
- **Stateless Auth:** Implemented **Spring Security 6** with **JWT (JSON Web Tokens)**.
- **RBAC:** Fine-grained **Role-Based Access Control** implemented at the method level using `@PreAuthorize`.
- **Custom Identity:** Integrated a custom `UserDetailsService` with dynamic role-prefixing (`ROLE_`) for full Spring Security compatibility.

### 5. Automated Infrastructure Verification
- **Testcontainers:** Eliminated "works on my machine" issues by using real **PostgreSQL** and **Kafka** Docker containers for integration testing.
- **Isolated Testing:** Enforced strict test isolation using transactional rollbacks and dynamic Kafka group IDs.

## Tech Stack
- **Backend:** Java 17 (LTS), Spring Boot 3.2.5, JPA/Hibernate.
- **Security:** Spring Security 6, JJWT (v0.11.5).
- **Middleware:** Apache Kafka, Redis 7 (Alpine).
- **Database:** PostgreSQL 16.
- **Tools:** MapStruct (Type-safe mapping), Lombok, Jakarta Validation.
- **Documentation:** SpringDoc OpenAPI / Swagger UI.

## Testing Strategy
I follow a "Safety-First" build philosophy. Every build is verified by:
1. **Unit Tests:** Mockito-based isolated logic testing.
2. **Full-Cycle Integration Tests:** Real infrastructure verification using Testcontainers.
3. **Concurrency Tests:** Multi-threaded stress testing for race-condition prevention.

To run the full test suite:
```bash
mvn clean test