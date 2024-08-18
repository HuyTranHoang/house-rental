INSERT INTO users (id, username, password, email, phone_number, first_name, last_name, avatar_url, is_active,
                   is_non_locked, is_deleted, created_at, updated_at)
VALUES (2, 'user', '$2a$10$aPFK5XoRGGEu/zMu/P11z.yO/QxtJyVUQD4euihW.A428tTVHt3je', 'user@gmail.com', '0123456789',
        'User', 'Test',
        'https://robohash.org/user?set=set4', true, true, false, random_date_within_14_days(), now()),
       (1, 'admin', '$2a$10$aPFK5XoRGGEu/zMu/P11z.yO/QxtJyVUQD4euihW.A428tTVHt3je', 'admin@gmail.com', '0123456789',
        'Admin', 'Test',
        'https://robohash.org/admin?set=set4', true, true, false, random_date_within_14_days(), now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_authorities (user_id, authority_id)
VALUES (2, 1),
       (2, 2),
       (1, 33)
ON CONFLICT (user_id, authority_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2)
ON CONFLICT (user_id, role_id) DO NOTHING;
