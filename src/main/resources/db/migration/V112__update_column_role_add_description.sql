ALTER TABLE roles ADD COLUMN description VARCHAR(512);

UPDATE roles SET description = 'Có quyền quản lý toàn bộ hệ thống, người dùng, dữ liệu. Thực hiện mọi tác vụ trên hệ thống.' WHERE roles.name = 'ROLE_ADMIN';
UPDATE roles SET description = 'Người dùng, có thể thay đổi thông tin cá nhân.' WHERE roles.name = 'ROLE_USER';

INSERT INTO authorities (privilege)
VALUE ('dashboard:read');