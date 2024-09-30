DROP TABLE IF EXISTS notifications;
CREATE TABLE IF NOT EXISTS notifications
(
    id          SERIAL PRIMARY KEY,
    user_id     INT,
    property_id INT,
    comment_id  INT,
    is_seen     BOOLEAN     DEFAULT FALSE,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE ON UPDATE CASCADE
);