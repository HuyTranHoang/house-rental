-- Hàm tạo ngày ngẫu nhiên trong 14 ngày qua
CREATE OR REPLACE FUNCTION random_date_within_14_days() RETURNS TIMESTAMPTZ
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN NOW() - INTERVAL '1 second' * FLOOR(RANDOM() * 14 * 24 * 60 * 60);
END;
$$;

-- Chèn dữ liệu vào bảng cities
INSERT INTO cities(id, name, created_at, updated_at)
VALUES
    (1, 'Hồ Chí Minh', random_date_within_14_days(), NOW()),
    (2, 'Hà Nội', random_date_within_14_days(), NOW());

-- Chèn dữ liệu vào bảng districts
INSERT INTO districts(id, name, city_id, created_at, updated_at)
VALUES
    (1, 'Quận 1', 1, random_date_within_14_days(), NOW()),
    (2, 'Quận 2 (TP. Thủ Đức)', 1, random_date_within_14_days(), NOW()),
    (3, 'Quận 3', 1, random_date_within_14_days(), NOW()),
    (4, 'Quận 4', 1, random_date_within_14_days(), NOW()),
    (5, 'Quận 5', 1, random_date_within_14_days(), NOW()),
    (6, 'Quận 6', 1, random_date_within_14_days(), NOW()),
    (7, 'Quận 7', 1, random_date_within_14_days(), NOW()),
    (8, 'Quận 8', 1, random_date_within_14_days(), NOW()),
    (9, 'Quận 9', 1, random_date_within_14_days(), NOW()),
    (10, 'Quận 10', 1, random_date_within_14_days(), NOW()),
    (11, 'Quận 11', 1, random_date_within_14_days(), NOW()),
    (12, 'Quận 12', 1, random_date_within_14_days(), NOW()),
    (13, 'Quận Bình Thạnh', 1, random_date_within_14_days(), NOW()),
    (14, 'Quận Gò Vấp', 1, random_date_within_14_days(), NOW()),
    (15, 'Quận Phú Nhuận', 1, random_date_within_14_days(), NOW()),
    (16, 'Quận Tân Bình', 1, random_date_within_14_days(), NOW()),
    (17, 'Quận Tân Phú', 1, random_date_within_14_days(), NOW()),
    (18, 'Quận Thủ Đức (TP. Thủ Đức)', 1, random_date_within_14_days(), NOW()),
    (19, 'Quận Bình Tân', 1, random_date_within_14_days(), NOW()),
    (20, 'Huyện Bình Chánh', 1, random_date_within_14_days(), NOW()),
    (21, 'Huyện Cần Giờ', 1, random_date_within_14_days(), NOW()),
    (22, 'Huyện Củ Chi', 1, random_date_within_14_days(), NOW()),
    (23, 'Huyện Hóc Môn', 1, random_date_within_14_days(), NOW()),
    (24, 'Huyện Nhà Bè', 1, random_date_within_14_days(), NOW()),
    (25, 'Quận Ba Đình', 2, random_date_within_14_days(), NOW()),
    (26, 'Quận Hoàn Kiếm', 2, random_date_within_14_days(), NOW()),
    (27, 'Quận Tây Hồ', 2, random_date_within_14_days(), NOW()),
    (28, 'Quận Long Biên', 2, random_date_within_14_days(), NOW()),
    (29, 'Quận Cầu Giấy', 2, random_date_within_14_days(), NOW()),
    (30, 'Quận Đống Đa', 2, random_date_within_14_days(), NOW()),
    (31, 'Quận Hai Bà Trưng', 2, random_date_within_14_days(), NOW()),
    (32, 'Quận Hoàng Mai', 2, random_date_within_14_days(), NOW()),
    (33, 'Quận Thanh Xuân', 2, random_date_within_14_days(), NOW()),
    (34, 'Huyện Sóc Sơn', 2, random_date_within_14_days(), NOW()),
    (35, 'Huyện Đông Anh', 2, random_date_within_14_days(), NOW()),
    (36, 'Huyện Gia Lâm', 2, random_date_within_14_days(), NOW()),
    (37, 'Huyện Thanh Trì', 2, random_date_within_14_days(), NOW()),
    (38, 'Huyện Ba Vì', 2, random_date_within_14_days(), NOW()),
    (39, 'Huyện Phúc Thọ', 2, random_date_within_14_days(), NOW()),
    (40, 'Huyện Thạch Thất', 2, random_date_within_14_days(), NOW()),
    (50, 'Huyện Quốc Oai', 2, random_date_within_14_days(), NOW()),
    (51, 'Huyện Chương Mỹ', 2, random_date_within_14_days(), NOW()),
    (52, 'Huyện Đan Phượng', 2, random_date_within_14_days(), NOW()),
    (53, 'Huyện Hoài Đức', 2, random_date_within_14_days(), NOW()),
    (54, 'Huyện Thanh Oai', 2, random_date_within_14_days(), NOW()),
    (55, 'Huyện Mỹ Đức', 2, random_date_within_14_days(), NOW()),
    (56, 'Huyện Ứng Hòa', 2, random_date_within_14_days(), NOW()),
    (57, 'Huyện Thường Tín', 2, random_date_within_14_days(), NOW());
