CREATE TABLE IF NOT EXISTS payments (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id          UUID            NOT NULL,
    beneficiary         VARCHAR(255)    NOT NULL,
    amount              NUMERIC(19, 2)  NOT NULL,
    currency            VARCHAR(3)      NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    idempotency_key     VARCHAR(255)    NOT NULL UNIQUE,
    confirmed_at        TIMESTAMPTZ,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    CONSTRAINT chk_payment_status CHECK (status IN ('PENDING', 'CONFIRMED', 'FAILED')),
    CONSTRAINT chk_payment_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_payment_currency CHECK (currency ~ '^[A-Z]{3}$')
);

CREATE INDEX idx_payments_account_id ON payments (account_id);
CREATE INDEX idx_payments_status ON payments (status);
CREATE INDEX idx_payments_idempotency_key ON payments (idempotency_key);
