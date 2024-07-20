DROP TABLE if EXISTS properties;
CREATE TABLE IF NOT EXISTS properties (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    location VARCHAR(255) NOT NULL,
    area DECIMAL(10, 2),
    num_rooms INT,
    status ENUM('PENDING', 'RESOLVED') DEFAULT 'PENDING',
    user_id INT,
    city_id INT,
    district_id INT,
    room_type_id INT,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) on delete cascade on update cascade,
    FOREIGN KEY (city_id) REFERENCES cities(id) on delete cascade on update cascade,
    FOREIGN KEY (district_id) REFERENCES districts(id) on delete cascade on update cascade,
    FOREIGN KEY (room_type_id) REFERENCES room_types(id) on delete cascade on update cascade
);

DROP TABLE if EXISTS amenities;
CREATE TABLE IF NOT EXISTS amenities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE if EXISTS property_amenities;
CREATE TABLE IF NOT EXISTS property_amenities (
    property_id INT,
    amenity_id INT,
    PRIMARY KEY (property_id, amenity_id),
    FOREIGN KEY (property_id) REFERENCES properties(id)  on delete cascade on update cascade,
    FOREIGN KEY (amenity_id) REFERENCES amenities(id)  on delete cascade on update cascade
);
