ALTER TABLE properties
    ALTER COLUMN is_priority SET DEFAULT FALSE,
    ALTER COLUMN refresh_day SET DEFAULT TIMESTAMP '1970-01-01 00:00:00',
    ALTER COLUMN priority_expiration SET DEFAULT TIMESTAMP '1970-01-01 00:00:00';