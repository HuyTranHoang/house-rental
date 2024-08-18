DROP TABLE IF EXISTS properties;
CREATE TABLE IF NOT EXISTS properties
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255)   NOT NULL,
    description  TEXT,
    price        DECIMAL(10, 2) NOT NULL,
    location     VARCHAR(255)   NOT NULL,
    area         DECIMAL(10, 2),
    num_rooms    INT,
    status       TEXT CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')) DEFAULT 'PENDING',
    user_id      INT,
    city_id      INT,
    district_id  INT,
    room_type_id INT,
    is_blocked   BOOLEAN        NOT NULL                        DEFAULT FALSE,
    is_deleted   BOOLEAN        NOT NULL                        DEFAULT FALSE,
    created_at   TIMESTAMPTZ                                    DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ                                    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (district_id) REFERENCES districts (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_type_id) REFERENCES room_types (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS amenities;
CREATE TABLE IF NOT EXISTS amenities
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS property_amenities;
CREATE TABLE IF NOT EXISTS property_amenities
(
    property_id INT,
    amenity_id  INT,
    public_id   VARCHAR(255),
    PRIMARY KEY (property_id, amenity_id),
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (amenity_id) REFERENCES amenities (id) ON DELETE CASCADE ON UPDATE CASCADE
);
