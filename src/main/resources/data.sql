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
       (1, 2),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4);

INSERT IGNORE INTO users (id, username, password, email, phone_number, first_name, last_name, avatar_url, is_active,
                          is_non_locked, is_deleted, created_at, updated_at)

VALUES (1, 'user', '$2a$10$aPFK5XoRGGEu/zMu/P11z.yO/QxtJyVUQD4euihW.A428tTVHt3je', 'user@gmail.com', '0123456789',
        'User', 'Test',
        'http://localhost:8080/api/user/image/profile/user@email.com', 1, 1, 0, now(), now()),
       (2, 'admin', '$2a$10$aPFK5XoRGGEu/zMu/P11z.yO/QxtJyVUQD4euihW.A428tTVHt3je', 'admin@gmail.com', '0123456789',
        'Admin', 'Test',
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


INSERT IGNORE INTO properties(id, title, description, price, location, area, num_rooms, status, user_id, city_id,
                              district_id, room_type_id, is_deleted, created_at, updated_at)
VALUES (1, 'test title', 'test description', 300000, 'test location', 111, 1, 'PENDING', 1, 1, 1, 1, 0, now(), now()),
       (2, 'Phòng rất đẹp 7A/9 Thành Thái, P.14, Quận 10 (Khoá Vân Tay, giờ tự do)', 'Phòng cho thuê thiết kế rất đẹp có nhiều tiện ích tuyệt vời ngay trung tâm Quận 10, địa chỉ: 7A/9 Thành Thái, Phường 14, Quận 10, TP. HCM. (Hẻm 7 Thành Thái, Cư xá Đồng Tiến, vào 500m có đường 7A Thành Thái).
+ DT: 20m2, Phòng đẹp thiết kế đẹp với nhiều tiện ích tuyệt vời: có Máy Lạnh, kệ bếp đá hoa cương có bồn rửa chén, Wc riêng.
+ Có Máy lạnh tiết kiệm điện.
+ WC riêng rộng rãi, cửa sổ thoáng mát.
+ Có người dọn vệ sinh hành lang hàng tuần, có internet cáp quang.
+ Có bảo vệ 24/24h, giờ giấc tự do cho bạn cảm giác như ở nhà mình.
+ Máy giặt miễn phí cho toà nhà thuận tiện cho bạn.
+ Khoá vân tay, camera an ninh khắp toà nhà, cho bạn cảm giác thoải mái như chính nhà mình.
+ Nhà mặt tiền đường xe hơi trong khu dân cư an ninh sầm uất khu dân trí cao, tiện đi lại mọi nơi trong thành phố,

+ Gần chợ Thành Thái, siêu thị, nhà sách. Gần nhiều trường đại học: ĐH Bách Khoa (300m), ĐH Kinh Tế, ĐH Y Khoa Phạm Ngọc Thạch, ĐH Ngoại Ngữ - Tin Học, ĐH Hoa Sen.
+ Gần bệnh viện đa khoa Quận 10, bệnh viện 115, bệnh Viện Nhi Đồng (600m).
- Là chỗ ở lý tưởng cho gia đình, nhân viên văn phòng, sinh viên ở.

Giá rất hợp lý mùa dịch: 3.2 Triệu/tháng.
Liên Hệ: 0937554570 (A.Thái)

TIỀN PHÒNG GIẢM SÂU VÀ KHÔNG TĂNG GIÁ.', 3200000, '7A/9 Thành Thái, P.14, Quận 10', 20, 1, 'PENDING', 3, 1, 10, 1, 0,
        now(),
        now());

INSERT IGNORE INTO property_amenities(property_id, amenity_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5);

INSERT IGNORE INTO property_images(property_id, image_url)
VALUES (1, 'http://localhost:8080/api/property/image/1'),
       (2, 'https://cloud.mogi.vn/images/2024/05/13/006/8c555c3ed46846f28e6ce640cd8be5de.jpg'),
       (2, 'https://cloud.mogi.vn/images/2024/06/13/145/cb463a3f4d604ac5ab75db9870b2b2b5.jpg'),
       (2, 'https://cloud.mogi.vn/images/2024/06/13/146/52e87e84b654415b8d0d5330add313e4.jpg'),
       (2, 'https://cloud.mogi.vn/images/2024/06/13/147/e1070de59c4d483f87450a497adef8b8.jpg');

INSERT IGNORE INTO reviews(id, user_id, property_id, rating, comment, is_deleted, created_at, updated_at)
VALUES (1, 1, 1, 5, 'test comment', 0, now(), now());