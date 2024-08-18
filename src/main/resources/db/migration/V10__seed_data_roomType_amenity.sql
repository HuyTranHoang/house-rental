-- Chèn dữ liệu vào bảng room_types
INSERT INTO room_types(id, name, created_at, updated_at)
VALUES
    (1, 'Phòng trọ', random_date_within_14_days(), NOW()),
    (2, 'Chung cư mini', random_date_within_14_days(), NOW()),
    (3, 'Nhà nguyên căn', random_date_within_14_days(), NOW()),
    (4, 'Chung cư', random_date_within_14_days(), NOW()),
    (5, 'Ký túc xá', random_date_within_14_days(), NOW());

-- Chèn dữ liệu vào bảng amenities
INSERT INTO amenities(id, name, created_at, updated_at)
VALUES
    (1, 'Máy lạnh', random_date_within_14_days(), NOW()),
    (2, 'Máy nước nóng', random_date_within_14_days(), NOW()),
    (3, 'Tủ lạnh', random_date_within_14_days(), NOW()),
    (4, 'Máy giặt', random_date_within_14_days(), NOW()),
    (5, 'Bếp ga', random_date_within_14_days(), NOW()),
    (6, 'Bếp từ', random_date_within_14_days(), NOW()),
    (7, 'Tivi', random_date_within_14_days(), NOW()),
    (8, 'Tủ quần áo', random_date_within_14_days(), NOW()),
    (9, 'Giường', random_date_within_14_days(), NOW()),
    (10, 'Bàn làm việc', random_date_within_14_days(), NOW()),
    (11, 'Wifi', random_date_within_14_days(), NOW()),
    (12, 'Chỗ để xe', random_date_within_14_days(), NOW()),
    (13, 'Ban công', random_date_within_14_days(), NOW()),
    (14, 'Hồ bơi', random_date_within_14_days(), NOW()),
    (15, 'Phòng tập gym', random_date_within_14_days(), NOW()),
    (16, 'Sân thượng', random_date_within_14_days(), NOW()),
    (17, 'Sân vườn', random_date_within_14_days(), NOW()),
    (18, 'Thang máy', random_date_within_14_days(), NOW()),
    (19, 'An ninh', random_date_within_14_days(), NOW()),
    (20, 'Dịch vụ giặt ủi', random_date_within_14_days(), NOW()),
    (21, 'Dịch vụ vệ sinh', random_date_within_14_days(), NOW()),
    (22, 'Dịch vụ giữ xe', random_date_within_14_days(), NOW()),
    (23, 'Dịch vụ giữ trẻ', random_date_within_14_days(), NOW()),
    (24, 'Dịch vụ hỗ trợ sinh hoạt', random_date_within_14_days(), NOW()),
    (25, 'Dịch vụ hỗ trợ tìm việc', random_date_within_14_days(), NOW()),
    (26, 'Dịch vụ hỗ trợ học tập', random_date_within_14_days(), NOW()),
    (27, 'Dịch vụ hỗ trợ sức khỏe', random_date_within_14_days(), NOW()),
    (28, 'Dịch vụ hỗ trợ tài chính', random_date_within_14_days(), NOW()),
    (29, 'Dịch vụ hỗ trợ pháp lý', random_date_within_14_days(), NOW()),
    (30, 'Dịch vụ hỗ trợ tâm lý', random_date_within_14_days(), NOW()),
    (31, 'Dịch vụ hỗ trợ du lịch', random_date_within_14_days(), NOW()),
    (32, 'Dịch vụ hỗ trợ mua sắm', random_date_within_14_days(), NOW()),
    (33, 'Dịch vụ hỗ trợ vận chuyển', random_date_within_14_days(), NOW()),
    (34, 'Dịch vụ hỗ trợ nấu ăn', random_date_within_14_days(), NOW()),
    (35, 'Dịch vụ hỗ trợ chăm sóc thú cưng', random_date_within_14_days(), NOW());
