DROP TABLE IF EXISTS advertisements;
CREATE TABLE advertisements
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   TEXT,
    start_date    DATE,
    end_date      DATE,
    discount_code VARCHAR(50),
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP
);
