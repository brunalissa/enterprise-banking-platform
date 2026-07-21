CREATE SCHEMA IF NOT EXISTS transaction;

CREATE TABLE IF NOT EXISTS transaction.transactions (
    id                  UUID PRIMARY KEY,
    customer_id         UUID NOT NULL,
    source_account_id   UUID NOT NULL,
    target_account_id    UUID NOT NULL,
    source_account_number VARCHAR(30),
    target_account_number  VARCHAR(30),
    amount              DECIMAL(19,4) NOT NULL,
    currency            VARCHAR(3) NOT NULL,
    type                VARCHAR(20) NOT NULL CHECK (type IN ('TRANSFER','DEPOSIT','WITHDRAWAL','PAYMENT')),
    status              VARCHAR(20) NOT NULL CHECK (status IN ('PENDING','PROCESSING','COMPLETED','FAILED','COMPENSATED')),
    reference           VARCHAR(100),
    description         VARCHAR(500),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at        TIMESTAMP
);

CREATE INDEX idx_tx_customer_id ON transaction.transactions(customer_id);
CREATE INDEX idx_tx_source_account ON transaction.transactions(source_account_id);
CREATE INDEX idx_tx_target_account ON transaction.transactions(target_account_id);
CREATE INDEX idx_tx_status ON transaction.transactions(status);
CREATE INDEX idx_tx_reference ON transaction.transactions(reference);

-- Outbox table for the Outbox Pattern
CREATE TABLE IF NOT EXISTS transaction.outbox_messages (
    id              UUID PRIMARY KEY,
    aggregate_type   VARCHAR(50) NOT NULL,
    aggregate_id     UUID NOT NULL,
    event_type       VARCHAR(100) NOT NULL,
    payload          VARCHAR(4000) NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','PROCESSED','FAILED')),
    created_at       TIMESTAMP NOT NULL DEFAULT NOW(),
    processed_at     TIMESTAMP,
    retry_count      INTEGER DEFAULT 0
);

CREATE INDEX idx_outbox_status ON transaction.outbox_messages(status);

-- Idempotency key table
CREATE TABLE IF NOT EXISTS transaction.idempotency_keys (
    key             VARCHAR(100) PRIMARY KEY,
    transaction_id  UUID,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_idempotency_transaction ON transaction.idempotency_keys(transaction_id);

-- Saga state table
CREATE TABLE IF NOT EXISTS transaction.saga_steps (
    id              UUID PRIMARY KEY,
    saga_id         UUID NOT NULL,
    step_name       VARCHAR(100) NOT NULL,
    status          VARCHAR(30) NOT NULL CHECK (status IN ('STARTED','COMPLETED','FAILED','COMPENSATED','COMPENSATION_FAILED')),
    error_message   VARCHAR(1000),
    started_at      TIMESTAMP NOT NULL,
    completed_at    TIMESTAMP
);

CREATE INDEX idx_saga_id ON transaction.saga_steps(saga_id);
