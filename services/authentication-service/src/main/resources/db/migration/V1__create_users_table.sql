CREATE TABLE users (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    username        VARCHAR(50)     NOT NULL UNIQUE,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,
    role            VARCHAR(20)     NOT NULL CHECK (role IN ('ADMIN','EMPLOYEE','CUSTOMER')),
    active          BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- Indexes for fast lookups
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(active);

-- Audit table
CREATE TABLE users_audit (
    audit_id    BIGSERIAL       PRIMARY KEY,
    user_id     UUID            NOT NULL,
    action      VARCHAR(20)     NOT NULL,
    changed_by  VARCHAR(50),
    changed_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    old_values  JSONB,
    new_values  JSONB
);

CREATE INDEX idx_users_audit_user_id ON users_audit(user_id);
CREATE INDEX idx_users_audit_changed_at ON users_audit(changed_at);