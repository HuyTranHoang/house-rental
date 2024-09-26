ALTER TABLE reviews
    DROP COLUMN rating;

ALTER TABLE reviews
    RENAME TO comments;