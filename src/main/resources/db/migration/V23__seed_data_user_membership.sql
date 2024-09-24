INSERT INTO user_memberships (user_id, membership_id, start_date, end_date, total_priority_limit, total_refresh_limit, status)
VALUES (2, 1,now(), now(), 1, 1, 'EXPIRED'),
       (1, 2, now(), now(), 4, 4, 'EXPIRED')