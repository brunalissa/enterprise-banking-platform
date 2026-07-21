CREATE SCHEMA IF NOT EXISTS account;

CREATE TABLE IF NOT EXISTS account.accounts (
    id              UUID PRIMARY KEY,
    customer_id     UUID NOT NULL,
    account_number  VARCHAR(30) NOT NULL UNIQUE,
    type            VARCHAR(20) NOT NULL CHECK (type IN ('CHECKING','SAVINGS','BUSINESS','JOINT')),
    balance         DECIMAL(19,4) NOT NULL DEFAULT 0,
    currency        VARCHAR(3) NOT NULL,
    status          VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE','FROZEN','CLOSED','PENDING_ACTIVATION')),
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_accounts_customer_id ON account.accounts(customer_id);
CREATE INDEX idx_accounts_account_number ON account.accounts(account_number);
CREATE INDEX idx_accounts_status ON account.accounts(status);

CREATE TABLE IF NOT EXISTS account.account_transactions (
    id              UUID PRIMARY KEY,
    account_id      UUID NOT NULL REFERENCES account.accounts(id),
    type            VARCHAR(20) NOT NULL,
    amount          DECIMAL(19,4) NOT NULL,
    currency        VARCHAR(3) NOT NULL,
    reference       VARCHAR(255),
    balance_after   DECIMAL(19,4) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_account_tx_account_id ON account.account_transactions(account_id);
CREATE INDEX idx_account_tx_created_at ON account.account_transactions(created_at);
