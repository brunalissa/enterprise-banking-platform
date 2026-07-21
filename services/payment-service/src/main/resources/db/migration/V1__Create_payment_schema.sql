CREATE SCHEMA IF NOT EXISTS payment;
CREATE TABLE IF NOT EXISTS payment.payments (
    id              UUID PRIMARY KEY,
    customer_id     UUID NOT NULL,
    account_id      UUID NOT NULL,
    payee           VARCHAR(255) NOT NULL,
    payee_account   VARCHAR(30) NOT NULL,
    amount          DECIMAL(19,4) NOT NULL,
    currency        VARCHAR(3) NOT NULL,
    type            VARCHAR(30) NOT NULL CHECK (type IN ('BILL_PAYMENT','P2P_TRANSFER','MERCHANT_PAYMENT','INTERNAL_TRANSFER')),
    status          VARCHAR(20) NOT NULL CHECK (status IN ('INITIATED','PROCESSING','CONFIRMED','FAILED','REFUNDED')),
    reference       VARCHAR(100),
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    confirmed_at    TIMESTAMP
);
CREATE INDEX idx_payments_customer ON payment.payments(customer_id);
CREATE INDEX idx_payments_status ON payment.payments(status);
CREATE INDEX idx_payments_idempotency ON payment.payments(idempotency_key);
