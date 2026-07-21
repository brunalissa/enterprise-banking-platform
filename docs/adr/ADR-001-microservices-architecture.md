# ADR-001: Microservices Architecture

## Status
Accepted

## Date
2026-07-20

## Context

The Enterprise Banking Platform needs to handle multiple business domains: customer management, account management, transactions, payments, notifications, and fraud detection. The system must be scalable, maintainable, and allow independent deployment of each domain.

A monolithic architecture would couple all domains together, making it difficult to scale individual components, deploy independently, or adopt different technology choices per domain.

## Decision

We will adopt a **microservices architecture** with the following services:

| Service | Port | Database | Responsibility |
|---------|------|----------|----------------|
| API Gateway | 8080 | - | Routing, rate limiting, JWT validation |
| Authentication Service | 8081 | banking_auth | JWT tokens, user management, RBAC |
| Customer Service | 8082 | banking_customer | Customer profiles, KYC, personal data |
| Account Service | 8083 | banking_account | Bank accounts, balances, history |
| Transaction Service | 8084 | banking_transaction | Money transfers, Saga orchestration |
| Payment Service | 8085 | banking_payment | Payment processing, idempotency |
| Notification Service | 8086 | banking_notification | Email/SMS/push notifications |
| Fraud Detection Service | 8087 | banking_fraud | Risk analysis, suspicious activity detection |

Each service has:
- Its own Spring Boot application
- Its own PostgreSQL database schema
- Independent REST API
- Own Dockerfile
- Kafka integration for event-driven communication

## Consequences

### Advantages
- **Independent deployment**: Each service can be deployed without affecting others
- **Independent scaling**: Transaction and payment services can scale independently
- **Technology flexibility**: Each service can evolve independently
- **Fault isolation**: A failure in one service doesn't bring down the entire platform
- **Team autonomy**: Different teams can own different services

### Disadvantages
- **Operational complexity**: 8 services require monitoring, logging, and tracing
- **Network overhead**: Inter-service communication adds latency
- **Data consistency**: Distributed transactions require Saga pattern
- **Deployment complexity**: Requires container orchestration (Kubernetes)

## Alternatives Considered

1. **Modular Monolith**: Single application with modular design. Rejected because we need independent scaling and deployment for a banking platform.
2. **Service-Oriented Architecture (SOA)**: With ESB. Rejected due to ESB bottleneck and coupling concerns.
3. **Event-Driven Architecture only**: Rejected because REST APIs are needed for synchronous client requests.