DROP TABLE IF EXISTS favorites;
CREATE TABLE IF NOT EXISTS favorites
(
    user_id     INT,
    property_id INT,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, property_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS property_images;
CREATE TABLE IF NOT EXISTS property_images
(
    id          SERIAL PRIMARY KEY,
    property_id INT,
    image_url   VARCHAR(255) NOT NULL,
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE ON UPDATE CASCADE
);
