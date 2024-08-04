CREATE FUNCTION random_date_within_14_days() RETURNS DATETIME
    DETERMINISTIC
BEGIN
    RETURN NOW() - INTERVAL FLOOR(RAND() * 14 * 24 * 60 * 60) SECOND;
END;

INSERT INTO cities(id, name, created_at, updated_at)
VALUES (1, 'Hồ Chí Minh', random_date_within_14_days(), now()),
       (2, 'Hà Nội', random_date_within_14_days(), now());

INSERT INTO districts(id, name, city_id, created_at, updated_at)
VALUES (1, 'Quận 1', 1, random_date_within_14_days(), now()),
       (2, 'Quận 2 (TP. Thủ Đức)', 1, random_date_within_14_days(), now()),
       (3, 'Quận 3', 1, random_date_within_14_days(), now()),
       (4, 'Quận 4', 1, random_date_within_14_days(), now()),
       (5, 'Quận 5', 1, random_date_within_14_days(), now()),
       (6, 'Quận 6', 1, random_date_within_14_days(), now()),
       (7, 'Quận 7', 1, random_date_within_14_days(), now()),
       (8, 'Quận 8', 1, random_date_within_14_days(), now()),
       (9, 'Quận 9', 1, random_date_within_14_days(), now()),
       (10, 'Quận 10', 1, random_date_within_14_days(), now()),
       (11, 'Quận 11', 1, random_date_within_14_days(), now()),
       (12, 'Quận 12', 1, random_date_within_14_days(), now()),
       (13, 'Quận Bình Thạnh', 1, random_date_within_14_days(), now()),
       (14, 'Quận Gò Vấp', 1, random_date_within_14_days(), now()),
       (15, 'Quận Phú Nhuận', 1, random_date_within_14_days(), now()),
       (16, 'Quận Tân Bình', 1, random_date_within_14_days(), now()),
       (17, 'Quận Tân Phú', 1, random_date_within_14_days(), now()),
       (18, 'Quận Thủ Đức (TP. Thủ Đức)', 1, random_date_within_14_days(), now()),
       (19, 'Quận Bình Tân', 1, random_date_within_14_days(), now()),
       (20, 'Huyện Bình Chánh', 1, random_date_within_14_days(), now()),
       (21, 'Huyện Cần Giờ', 1, random_date_within_14_days(), now()),
       (22, 'Huyện Củ Chi', 1, random_date_within_14_days(), now()),
       (23, 'Huyện Hóc Môn', 1, random_date_within_14_days(), now()),
       (24, 'Huyện Nhà Bè', 1, random_date_within_14_days(), now()),
       (25, 'Quận Ba Đình', 2, random_date_within_14_days(), now()),
       (26, 'Quận Hoàn Kiếm', 2, random_date_within_14_days(), now()),
       (27, 'Quận Tây Hồ', 2, random_date_within_14_days(), now()),
       (28, 'Quận Long Biên', 2, random_date_within_14_days(), now()),
       (29, 'Quận Cầu Giấy', 2, random_date_within_14_days(), now()),
       (30, 'Quận Đống Đa', 2, random_date_within_14_days(), now()),
       (31, 'Quận Hai Bà Trưng', 2, random_date_within_14_days(), now()),
       (32, 'Quận Hoàng Mai', 2, random_date_within_14_days(), now()),
       (33, 'Quận Thanh Xuân', 2, random_date_within_14_days(), now()),
       (34, 'Huyện Sóc Sơn', 2, random_date_within_14_days(), now()),
       (35, 'Huyện Đông Anh', 2, random_date_within_14_days(), now()),
       (36, 'Huyện Gia Lâm', 2, random_date_within_14_days(), now()),
       (37, 'Huyện Thanh Trì', 2, random_date_within_14_days(), now()),
       (38, 'Huyện Ba Vì', 2, random_date_within_14_days(), now()),
       (39, 'Huyện Phúc Thọ', 2, random_date_within_14_days(), now()),
       (40, 'Huyện Thạch Thất', 2, random_date_within_14_days(), now()),
       (50, 'Huyện Quốc Oai', 2, random_date_within_14_days(), now()),
       (51, 'Huyện Chương Mỹ', 2, random_date_within_14_days(), now()),
       (52, 'Huyện Đan Phượng', 2, random_date_within_14_days(), now()),
       (53, 'Huyện Hoài Đức', 2, random_date_within_14_days(), now()),
       (54, 'Huyện Thanh Oai', 2, random_date_within_14_days(), now()),
       (55, 'Huyện Mỹ Đức', 2, random_date_within_14_days(), now()),
       (56, 'Huyện Ứng Hòa', 2, random_date_within_14_days(), now()),
       (57, 'Huyện Thường Tín', 2, random_date_within_14_days(), now());