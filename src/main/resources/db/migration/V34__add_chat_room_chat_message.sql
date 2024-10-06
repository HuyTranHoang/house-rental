CREATE TABLE chat_room
(
    id          VARCHAR(255) PRIMARY KEY,
    sender_id   INT       NOT NULL,
    receiver_id INT       NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE chat_message
(
    id           SERIAL PRIMARY KEY,
    chat_room_id VARCHAR(255) NOT NULL,
    sender_id    INT          NOT NULL,
    receiver_id  INT          NOT NULL,
    message      TEXT         NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_room_id) REFERENCES chat_room (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);