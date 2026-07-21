# ADR-003: PostgreSQL Database Choice

## Status
Accepted

## Date
2026-07-20

## Context

Each microservice needs a database for persistent storage. The banking platform has strict requirements for data integrity, ACID transactions, audit trails, and regulatory compliance.

## Decision

We will use **PostgreSQL 16** as the database for all microservices. Each service gets its own database/schema for isolation.

### Rationale

| Factor | PostgreSQL | MySQL | MongoDB |
|--------|-----------|-------|---------|
| ACID Compliance | ✅ Full | ✅ Partial | ❌ Limited |
| JSON Support | ✅ JSONB | ✅ JSON | ✅ Native |
| Audit Trail | ✅ Triggers + JSONB | ✅ Triggers | ⚠️ Manual |
| Performance | ✅ Excellent | ✅ Good | ✅ Good (read) |
| Schema Migrations | ✅ Flyway | ✅ Flyway | ⚠️ No schema |
| Community | ✅ Strong | ✅ Strong | ✅ Strong |

### Schema Strategy

- Each service owns its database (Database-per-Service pattern)
- Flyway manages migrations version-controlled
- Audit tables track all changes with JSONB diffs
- UUIDs for primary keys (globally unique, no sequential ID leakage)

## Consequences

### Advantages
- **ACID guarantees**: Critical for financial transactions
- **JSONB**: Flexible for audit logs and event payloads
- **Strong ecosystem**: Excellent support in Spring Data JPA
- **Proven reliability**: Used by major financial institutions

### Disadvantages
- **Operational overhead**: Multiple databases to manage
- **Distributed transactions**: Cross-service consistency requires Saga pattern
- **Horizontal scaling**: Read replicas needed for read-heavy services

## Alternatives Considered

1. **MySQL**: Simpler but weaker JSON support and audit capabilities
2. **MongoDB**: Better for document storage but lacks ACID guarantees needed for banking
3. **DynamoDB**: Would limit portability and local development