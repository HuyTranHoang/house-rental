INSERT INTO authorities (id, privilege, is_deleted, created_at, updated_at)
    VALUE (33, 'admin:all', 0, NOW(), NOW());

-- Delete existing records for the admin role in the role_authorities table
DELETE FROM role_authorities
WHERE role_id = (SELECT id FROM roles WHERE name = 'ROLE_ADMIN');

-- Insert the new admin:all authority for the admin role
INSERT INTO role_authorities (role_id, authority_id)
SELECT r.id, a.id
FROM roles r, authorities a
WHERE r.name = 'ROLE_ADMIN' AND a.privilege = 'admin:all';