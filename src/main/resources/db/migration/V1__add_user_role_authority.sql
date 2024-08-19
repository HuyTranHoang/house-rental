DROP TABLE IF EXISTS authorities;
CREATE TABLE IF NOT EXISTS authorities
(
    id         SERIAL PRIMARY KEY,
    privilege  VARCHAR(50) NOT NULL UNIQUE,
    is_deleted BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS roles;
CREATE TABLE IF NOT EXISTS roles
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(512),
    is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS role_authorities;
CREATE TABLE IF NOT EXISTS role_authorities
(
    role_id      INT,
    authority_id INT,
    PRIMARY KEY (role_id, authority_id),
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (authority_id) REFERENCES authorities (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone_number  VARCHAR(20),
    first_name    VARCHAR(100),
    last_name     VARCHAR(100),
    avatar_url    VARCHAR(255),
    balance       DECIMAL(10, 2)        DEFAULT 0.00,
    is_active     BOOLEAN      NOT NULL,
    is_non_locked BOOLEAN      NOT NULL,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS user_roles;
CREATE TABLE IF NOT EXISTS user_roles
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS user_authorities;
CREATE TABLE IF NOT EXISTS user_authorities
(
    user_id      INT,
    authority_id INT,
    PRIMARY KEY (user_id, authority_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (authority_id) REFERENCES authorities (id) ON DELETE CASCADE ON UPDATE CASCADE
);
