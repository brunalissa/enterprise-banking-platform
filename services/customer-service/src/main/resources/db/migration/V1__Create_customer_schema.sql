CREATE SCHEMA IF NOT EXISTS customer;

CREATE TABLE IF NOT EXISTS customer.customers (
    id            UUID PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone_number  VARCHAR(20),
    tax_id        VARCHAR(20) NOT NULL UNIQUE,
    date_of_birth DATE,
    address_id    UUID,
    street        VARCHAR(255),
    city          VARCHAR(100),
    state         VARCHAR(100),
    zip_code      VARCHAR(20),
    country       VARCHAR(100),
    status        VARCHAR(30) NOT NULL CHECK (status IN ('PENDING_VERIFICATION','ACTIVE','SUSPENDED','BLACKLISTED','CLOSED')),
    created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_customers_email ON customer.customers(email);
CREATE INDEX idx_customers_tax_id ON customer.customers(tax_id);
CREATE INDEX idx_customers_status ON customer.customers(status);

-- Audit table
CREATE TABLE IF NOT EXISTS customer.customer_audit (
    id          BIGSERIAL PRIMARY KEY,
    customer_id UUID NOT NULL,
    action      VARCHAR(50) NOT NULL,
    old_data     JSONB,
    new_data     JSONB,
    changed_by  VARCHAR(255),
    changed_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_customer_audit_customer_id ON customer.customer_audit(customer_id);
