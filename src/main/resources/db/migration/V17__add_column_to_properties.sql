ALTER TABLE properties
    ADD COLUMN is_priority         BOOLEAN DEFAULT FALSE,
    ADD COLUMN refresh_day         TIMESTAMP,
    ADD COLUMN priority_expiration TIMESTAMP;