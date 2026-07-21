CREATE TABLE IF NOT EXISTS notifications (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id     UUID            NOT NULL,
    channel         VARCHAR(20)     NOT NULL,
    type            VARCHAR(30)     NOT NULL,
    subject         VARCHAR(255)    NOT NULL,
    content         TEXT            NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'SENT',
    related_id      UUID,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    CONSTRAINT chk_channel CHECK (channel IN ('EMAIL', 'SMS', 'PUSH')),
    CONSTRAINT chk_type CHECK (type IN ('TRANSACTION_ALERT', 'ACCOUNT_NOTIFICATION')),
    CONSTRAINT chk_status CHECK (status IN ('SENT', 'FAILED'))
);

CREATE INDEX idx_notifications_customer_id ON notifications (customer_id);
CREATE INDEX idx_notifications_created_at ON notifications (created_at DESC);
