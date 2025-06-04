ALTER TABLE account_queue RENAME TO "position";

ALTER Table "position"
ADD queue_number smallint check (queue_number >= 1 and queue_number <= 100);