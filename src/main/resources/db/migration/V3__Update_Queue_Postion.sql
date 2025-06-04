ALTER Table queue
ADD owner_id bigint;

ALTER TABLE position
DROP COLUMN is_owner;