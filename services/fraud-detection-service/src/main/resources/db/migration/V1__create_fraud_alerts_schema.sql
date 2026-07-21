CREATE TYPE fraud_alert_status AS ENUM ('OPEN', 'REVIEWED', 'CLOSED');

CREATE TABLE IF NOT EXISTS fraud_alerts (
    id              UUID                    PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id  UUID                    NOT NULL,
    account_id      UUID                    NOT NULL,
    amount          NUMERIC(19, 4)          NOT NULL,
    reason          VARCHAR(255)            NOT NULL,
    risk_score      INTEGER                 NOT NULL,
    status          fraud_alert_status      NOT NULL DEFAULT 'OPEN',
    created_at      TIMESTAMPTZ             NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ             NOT NULL DEFAULT now(),
    CONSTRAINT chk_risk_score CHECK (risk_score >= 0 AND risk_score <= 100)
);

CREATE INDEX idx_fraud_alerts_status ON fraud_alerts (status);
CREATE INDEX idx_fraud_alerts_account_id ON fraud_alerts (account_id);
CREATE INDEX idx_fraud_alerts_transaction_id ON fraud_alerts (transaction_id);
