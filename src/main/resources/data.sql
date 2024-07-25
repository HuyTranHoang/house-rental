INSERT IGNORE INTO cities(id, name)
VALUES (1, 'Hồ Chí Minh'),
       (2, 'Hà Nội');

INSERT IGNORE INTO districts(id, name, city_id)
VALUES (1, 'Quận 1', 1),
       (2, 'Quận 2 (TP. Thủ Đức)', 1),
       (3, 'Quận 3', 1),
       (4, 'Quận 4', 1),
       (5, 'Quận 5', 1),
       (6, 'Quận 6', 1),
       (7, 'Quận 7', 1),
       (8, 'Quận 8', 1),
       (9, 'Quận 9', 1),
       (10, 'Quận 10', 1),
       (11, 'Quận 11', 1),
       (12, 'Quận 12', 1),
       (13, 'Quận Bình Thạnh', 1),
       (14, 'Quận Gò Vấp', 1),
       (15, 'Quận Phú Nhuận', 1),
       (16, 'Quận Tân Bình', 1),
       (17, 'Quận Tân Phú', 1),
       (18, 'Quận Thủ Đức (TP. Thủ Đức)', 1),
       (19, 'Quận Bình Tân', 1),
       (20, 'Huyện Bình Chánh', 1),
       (21, 'Huyện Cần Giờ', 1),
       (22, 'Huyện Củ Chi', 1),
       (23, 'Huyện Hóc Môn', 1),
       (24, 'Huyện Nhà Bè', 1),
       (25, 'Quận Ba Đình', 2),
       (26, 'Quận Hoàn Kiếm', 2),
       (27, 'Quận Tây Hồ', 2),
       (28, 'Quận Long Biên', 2),
       (29, 'Quận Cầu Giấy', 2),
       (30, 'Quận Đống Đa', 2),
       (31, 'Quận Hai Bà Trưng', 2),
       (32, 'Quận Hoàng Mai', 2),
       (33, 'Quận Thanh Xuân', 2),
       (34, 'Huyện Sóc Sơn', 2),
       (35, 'Huyện Đông Anh', 2),
       (36, 'Huyện Gia Lâm', 2),
       (37, 'Huyện Thanh Trì', 2),
       (38, 'Huyện Ba Vì', 2),
       (39, 'Huyện Phúc Thọ', 2),
       (40, 'Huyện Thạch Thất', 2),
       (50, 'Huyện Quốc Oai', 2),
       (51, 'Huyện Chương Mỹ', 2),
       (52, 'Huyện Đan Phượng', 2),
       (53, 'Huyện Hoài Đức', 2),
       (54, 'Huyện Thanh Oai', 2),
       (55, 'Huyện Mỹ Đức', 2),
       (56, 'Huyện Ứng Hòa', 2),
       (57, 'Huyện Thường Tín', 2);

INSERT IGNORE INTO room_types(id, name)
VALUES (1, 'Phòng trọ'),
       (2, 'Chung cư mini'),
       (3, 'Nhà nguyên căn'),
       (4, 'Chung cư'),
       (5, 'Ký túc xá');

INSERT IGNORE INTO amenities(id, name)
VALUES (1, 'Máy lạnh'),
       (2, 'Máy nước nóng'),
       (3, 'Tủ lạnh'),
       (4, 'Máy giặt'),
       (5, 'Bếp ga'),
       (6, 'Bếp từ'),
       (7, 'Tivi'),
       (8, 'Tủ quần áo'),
       (9, 'Giường'),
       (10, 'Bàn làm việc'),
       (11, 'Wifi'),
       (12, 'Chỗ để xe'),
       (13, 'Ban công'),
       (14, 'Hồ bơi'),
       (15, 'Phòng tập gym'),
       (16, 'Sân thượng'),
       (17, 'Sân vườn'),
       (18, 'Thang máy'),
       (19, 'An ninh'),
       (20, 'Dịch vụ giặt ủi'),
       (21, 'Dịch vụ vệ sinh'),
       (22, 'Dịch vụ giữ xe'),
       (23, 'Dịch vụ giữ trẻ'),
       (24, 'Dịch vụ hỗ trợ sinh hoạt'),
       (25, 'Dịch vụ hỗ trợ tìm việc'),
       (26, 'Dịch vụ hỗ trợ học tập'),
       (27, 'Dịch vụ hỗ trợ sức khỏe'),
       (28, 'Dịch vụ hỗ trợ tài chính'),
       (29, 'Dịch vụ hỗ trợ pháp lý'),
       (30, 'Dịch vụ hỗ trợ tâm lý'),
       (31, 'Dịch vụ hỗ trợ du lịch'),
       (32, 'Dịch vụ hỗ trợ mua sắm'),
       (33, 'Dịch vụ hỗ trợ vận chuyển'),
       (34, 'Dịch vụ hỗ trợ nấu ăn'),
       (35, 'Dịch vụ hỗ trợ chăm sóc thú cưng');

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


