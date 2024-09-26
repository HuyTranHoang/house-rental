DROP TABLE IF EXISTS comment_reports;
CREATE TABLE comment_reports
(
    id         SERIAL PRIMARY KEY,
    user_id    INT,
    comment_id INT,
    reason     TEXT    NOT NULL,
    status     TEXT CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')) DEFAULT 'PENDING',
    category   TEXT CHECK (category IN ('SCAM', 'INAPPROPRIATE_CONTENT', 'DUPLICATE', 'MISINFORMATION', 'OTHER'))
                                                                          DEFAULT 'OTHER',
    is_deleted BOOLEAN NOT NULL                                           DEFAULT FALSE,
    created_at TIMESTAMPTZ                                                DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE comments ADD COLUMN is_blocked BOOLEAN NOT NULL DEFAULT FALSE;