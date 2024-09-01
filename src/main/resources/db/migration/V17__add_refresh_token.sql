CREATE TABLE refresh_tokens
(
    id          SERIAL PRIMARY KEY,
    token       VARCHAR(512) NOT NULL,
    user_id     BIGINT       NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);