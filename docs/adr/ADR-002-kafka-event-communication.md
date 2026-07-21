# ADR-002: Kafka Event Communication

## Status
Accepted

## Date
2026-07-20

## Context

The microservices need to communicate with each other. Synchronous HTTP calls create tight coupling and cascading failures. When a transaction completes, multiple services need to be notified (notification, fraud detection, audit) without blocking the transaction workflow.

## Decision

We will use **Apache Kafka** as the event bus for asynchronous, event-driven communication between services.

### Event Topics

| Topic | Producer | Consumers |
|-------|----------|-----------|
| `customer-events` | customer-service | notification-service |
| `account-events` | account-service | notification-service |
| `transaction-events` | transaction-service | notification-service, fraud-detection-service |
| `payment-events` | payment-service | notification-service |
| `fraud-alerts` | fraud-detection-service | notification-service |

### Event Flow

```
Transaction Service → [transaction-events] → Notification Service (send alert)
                                         └→ Fraud Detection Service (analyze risk)
                                            ↓
                                        [fraud-alerts] → Notification Service (fraud alert)
```

### Patterns Implemented

1. **Outbox Pattern**: Events are written to an outbox table in the same transaction as the business data. A separate process reads and publishes them to Kafka. This ensures atomicity.

2. **Idempotency**: Each event has a unique `eventId`. Consumers track processed IDs to handle duplicates.

3. **Retry with DLQ**: Failed messages are retried with exponential backoff, then sent to a dead-letter queue.

## Consequences

### Advantages
- **Decoupling**: Services don't need to know about each other
- **Scalability**: Kafka handles high throughput with partitioning
- **Reliability**: Events persist even if consumers are temporarily down
- **Event replay**: Events can be replayed for debugging or recovery

### Disadvantages
- **Eventual consistency**: Not all services see the same state at the same time
- **Operational overhead**: Kafka cluster requires management
- **Complexity**: Event schemas, versioning, and ordering need careful design

## Alternatives Considered

1. **RabbitMQ**: Simpler but less suitable for high-throughput event streaming
2. **Direct HTTP calls**: Simpler but creates tight coupling and cascading failures
3. **AWS SNS/SQS**: Cloud-specific, would limit portability