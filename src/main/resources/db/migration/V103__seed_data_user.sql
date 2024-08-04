INSERT IGNORE INTO users (id, username, password, email, phone_number, first_name, last_name, avatar_url, is_active,
                          is_non_locked, is_deleted, created_at, updated_at)

VALUES (1, 'user', '$2a$10$aPFK5XoRGGEu/zMu/P11z.yO/QxtJyVUQD4euihW.A428tTVHt3je', 'user@gmail.com', '0123456789',
        'User', 'Test',
        'http://localhost:8080/api/user/image/profile/user@email.com', 1, 1, 0, random_date_within_14_days(), now()),
       (2, 'admin', '$2a$10$aPFK5XoRGGEu/zMu/P11z.yO/QxtJyVUQD4euihW.A428tTVHt3je', 'admin@gmail.com', '0123456789',
        'Admin', 'Test',
        'http://localhost:8080/api/user/image/profile/admin@email.com', 1, 1, 0, random_date_within_14_days(), now());

INSERT IGNORE INTO user_authorities (user_id, authority_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4);

INSERT IGNORE INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2);