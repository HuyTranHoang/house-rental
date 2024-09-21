DROP TABLE IF EXISTS user_memberships;
CREATE TABLE IF NOT EXISTS user_memberships
(
    id                   SERIAL PRIMARY KEY,
    user_id              INT REFERENCES users (id),
    membership_id        INT REFERENCES memberships (id),
    start_date           TIMESTAMP,
    end_date             TIMESTAMP,
    total_priority_limit INT,
    total_refresh_limit  INT,
    priority_posts_used  INT                                          DEFAULT 0,
    refreshes_posts_used INT                                          DEFAULT 0,
    status               TEXT CHECK (status IN ('ACTIVE', 'EXPIRED')) DEFAULT 'EXPIRED',
    is_deleted           BOOLEAN                                      DEFAULT FALSE,
    created_at           TIMESTAMP                                    DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP                                    DEFAULT CURRENT_TIMESTAMP
);
