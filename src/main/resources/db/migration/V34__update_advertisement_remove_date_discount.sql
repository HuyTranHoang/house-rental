ALTER TABLE advertisements DROP COLUMN IF EXISTS start_date,
                            DROP COLUMN IF EXISTS end_date,
                            DROP COLUMN IF EXISTS discount_code;

ALTER TABLE advertisements ADD COLUMN IF NOT EXISTS image_url VARCHAR(255);