CREATE TABLE IF NOT EXISTS customers (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name      VARCHAR(100)    NOT NULL,
    last_name       VARCHAR(100)    NOT NULL,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    phone           VARCHAR(30),
    date_of_birth   DATE            NOT NULL,
    street          VARCHAR(255),
    city            VARCHAR(100),
    state           VARCHAR(100),
    zip_code        VARCHAR(20),
    country         VARCHAR(100),
    document_id     VARCHAR(50)     NOT NULL UNIQUE,
    status          VARCHAR(20)     NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE')),
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_customers_email ON customers(email);
CREATE INDEX IF NOT EXISTS idx_customers_document_id ON customers(document_id);
CREATE INDEX IF NOT EXISTS idx_customers_status ON customers(status);

CREATE OR REPLACE FUNCTION update_customers_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_customers_updated_at ON customers;
CREATE TRIGGER trg_customers_updated_at
    BEFORE UPDATE ON customers
    FOR EACH ROW
    EXECUTE FUNCTION update_customers_updated_at();
