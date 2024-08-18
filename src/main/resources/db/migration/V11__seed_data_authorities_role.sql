-- Chèn dữ liệu vào bảng authorities
INSERT INTO authorities(id, privilege)
VALUES (1, 'user:read'),
       (2, 'user:update'),
       (3, 'user:create'),
       (4, 'user:delete'),
       (5, 'property:read'),
       (6, 'property:update'),
       (7, 'property:create'),
       (8, 'property:delete'),
       (9, 'review:read'),
       (10, 'review:update'),
       (11, 'review:create'),
       (12, 'review:delete'),
       (13, 'city:read'),
       (14, 'city:update'),
       (15, 'city:create'),
       (16, 'city:delete'),
       (17, 'district:read'),
       (18, 'district:update'),
       (19, 'district:create'),
       (20, 'district:delete'),
       (21, 'room_type:read'),
       (22, 'room_type:update'),
       (23, 'room_type:create'),
       (24, 'room_type:delete'),
       (25, 'amenity:read'),
       (26, 'amenity:update'),
       (27, 'amenity:create'),
       (28, 'amenity:delete'),
       (29, 'role:read'),
       (30, 'role:update'),
       (31, 'role:create'),
       (32, 'role:delete'),
       (33, 'admin:all'),
       (34, 'dashboard:read');

-- Chèn dữ liệu vào bảng roles
INSERT INTO roles(id, name, description)
VALUES (2, 'ROLE_USER', 'Người dùng, có thể thay đổi thông tin cá nhân.'),
       (1, 'ROLE_ADMIN', 'Có quyền quản lý toàn bộ hệ thống, người dùng, dữ liệu. Thực hiện mọi tác vụ trên hệ thống.');

-- Chèn dữ liệu vào bảng role_authorities
INSERT INTO role_authorities(role_id, authority_id)
VALUES (2, 1),
       (2, 2),
       (1, 33);
