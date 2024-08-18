DROP TABLE IF EXISTS reviews;
CREATE TABLE reviews
(
    id          SERIAL PRIMARY KEY,
    user_id     INT,
    property_id INT,
    rating      INT     NOT NULL,
    comment     TEXT,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS reports;
CREATE TABLE reports
(
    id          SERIAL PRIMARY KEY,
    user_id     INT,
    property_id INT,
    reason      TEXT    NOT NULL,
    status      TEXT CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')) DEFAULT 'PENDING',
    category    TEXT CHECK (category IN ('SPAM', 'INAPPROPRIATE_CONTENT', 'DUPLICATE', 'MISINFORMATION', 'OTHER'))
                                                                           DEFAULT 'OTHER',
    is_deleted  BOOLEAN NOT NULL                                           DEFAULT FALSE,
    created_at  TIMESTAMPTZ                                                DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE ON UPDATE CASCADE
);
