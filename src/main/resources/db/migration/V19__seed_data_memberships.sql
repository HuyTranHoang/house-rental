INSERT INTO memberships (name, price, duration_days, priority, refresh, description, is_deleted, created_at, updated_at)
VALUES ('Free', 00.00, 30, 1, 1, 'Hạng mức cơ bản', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Standard', 50.00, 30, 4, 4, 'Hạng mức tiêu chuẩn', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Premium', 99.00, 30, 10, 10, 'Hạng mức cao cấp', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
