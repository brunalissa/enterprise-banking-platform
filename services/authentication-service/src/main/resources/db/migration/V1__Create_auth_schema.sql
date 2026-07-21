CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE IF NOT EXISTS auth.users (
    id           UUID PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role         VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'CUSTOMER', 'EMPLOYEE')),
    status       VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE', 'LOCKED', 'SUSPENDED', 'PENDING_VERIFICATION')),
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON auth.users(email);
