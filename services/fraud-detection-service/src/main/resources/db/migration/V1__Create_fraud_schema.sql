CREATE SCHEMA IF NOT EXISTS fraud;
CREATE TABLE IF NOT EXISTS fraud.fraud_alerts (
    id              UUID PRIMARY KEY,
    transaction_id  UUID NOT NULL,
    customer_id     UUID NOT NULL,
    amount          DECIMAL(19,4) NOT NULL,
    risk_level      VARCHAR(20) NOT NULL CHECK (risk_level IN ('LOW','MEDIUM','HIGH','CRITICAL')),
    reason          VARCHAR(1000),
    status          VARCHAR(30) NOT NULL CHECK (status IN ('OPEN','INVESTIGATING','CONFIRMED_FRAUD','FALSE_POSITIVE')),
    detected_at     TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_fraud_customer ON fraud.fraud_alerts(customer_id);
CREATE INDEX idx_fraud_status ON fraud.fraud_alerts(status);
CREATE INDEX idx_fraud_risk_level ON fraud.fraud_alerts(risk_level);
