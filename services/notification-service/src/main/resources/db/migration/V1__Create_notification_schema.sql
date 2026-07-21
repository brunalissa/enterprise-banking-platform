CREATE SCHEMA IF NOT EXISTS notification;
CREATE TABLE IF NOT EXISTS notification.notifications (
    id           UUID PRIMARY KEY,
    customer_id   UUID NOT NULL,
    type         VARCHAR(30) NOT NULL CHECK (type IN ('EMAIL','SMS','PUSH','ACCOUNT_ALERT','TRANSACTION_ALERT','FRAUD_ALERT')),
    title        VARCHAR(255) NOT NULL,
    message      VARCHAR(1000) NOT NULL,
    recipient    VARCHAR(255),
    status       VARCHAR(20) NOT NULL CHECK (status IN ('PENDING','SENT','FAILED')),
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    sent_at      TIMESTAMP
);
CREATE INDEX idx_notifications_customer ON notification.notifications(customer_id);
CREATE INDEX idx_notifications_status ON notification.notifications(status);
