INSERT IGNORE INTO cities(name)
VALUES ('Hồ Chí Minh'),
       ('Hà Nội');

INSERT IGNORE INTO districts(name, city_id)
VALUES ('Quận 1', 1),
       ('Quận 2 (TP. Thủ Đức)', 1),
       ('Quận 3', 1),
       ('Quận 4', 1),
       ('Quận 5', 1),
       ('Quận 6', 1),
       ('Quận 7', 1),
       ('Quận 8', 1),
       ('Quận 9', 1),
       ('Quận 10', 1),
       ('Quận 11', 1),
       ('Quận 12', 1),
       ('Quận Bình Thạnh', 1),
       ('Quận Gò Vấp', 1),
       ('Quận Phú Nhuận', 1),
       ('Quận Tân Bình', 1),
       ('Quận Tân Phú', 1),
       ('Quận Thủ Đức (TP. Thủ Đức)', 1),
       ('Quận Bình Tân', 1),
       ('Huyện Bình Chánh', 1),
       ('Huyện Cần Giờ', 1),
       ('Huyện Củ Chi', 1),
       ('Huyện Hóc Môn', 1),
       ('Huyện Nhà Bè', 1);

INSERT IGNORE INTO authorities(id, privilege)
VALUES (1, 'user:read'),
       (2, 'user:update'),
       (3, 'user:create'),
       (4, 'user:delete');

INSERT IGNORE INTO roles (id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT IGNORE INTO role_authorities (role_id, authority_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4);

INSERT IGNORE INTO users (id, username, password, email, phone_number, first_name, last_name, avatar_url, is_active,
                   is_non_locked, is_deleted, created_at, updated_at)
VALUES (1, 'user', 'password', 'user@gmail.com', '0123456789', 'User', 'Test',
        'http://localhost:8080/api/user/image/profile/user@email.com', 1, 1, 0, now(), now()),
       (2, 'admin', 'password', 'admin@gmail.com', '0123456789', 'Admin', 'Test',
        'http://localhost:8080/api/user/image/profile/admin@email.com', 1, 1, 0, now(), now());

INSERT IGNORE INTO user_authorities (user_id, authority_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4);

INSERT IGNORE INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2);


