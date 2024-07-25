DROP TABLE IF EXISTS authorities;
CREATE TABLE IF NOT EXISTS authorities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS roles;
CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS role_authorities;
CREATE TABLE IF NOT EXISTS role_authorities (
    role_id INT,
    authority_id INT,
    PRIMARY KEY (role_id, authority_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) on delete cascade on update cascade,
    FOREIGN KEY (authority_id) REFERENCES authorities(id) on delete cascade on update cascade
);

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    avatar_url VARCHAR(255),
    is_active TINYINT(1) NOT NULL,
    is_non_locked TINYINT(1) NOT NULL,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS user_roles;
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) on delete cascade on update cascade,
    FOREIGN KEY (role_id) REFERENCES roles(id) on delete cascade on update cascade
);

DROP TABLE IF EXISTS user_authorities;
CREATE TABLE IF NOT EXISTS user_authorities (
    user_id INT,
    authority_id INT,
    PRIMARY KEY (user_id, authority_id),
    FOREIGN KEY (user_id) REFERENCES users(id) on delete cascade on update cascade,
    FOREIGN KEY (authority_id) REFERENCES authorities(id) on delete cascade on update cascade
);

# DROP TABLE IF EXISTS role_authorities;
# CREATE TABLE IF NOT EXISTS role_authorities (
#                                                 role_id INT,
#                                                 authority_id INT,
#                                                 PRIMARY KEY (role_id, authority_id),
#                                                 FOREIGN KEY (role_id) REFERENCES roles(id) on delete cascade on update cascade,
#                                                 FOREIGN KEY (authority_id) REFERENCES authorities(id) on delete cascade on update cascade
# );