INSERT INTO account(weight, length, height, gender, chipping_date_time, chipper_id, chipping_location_id)
VALUES(45.5, 80, 90.63, 'MALE', current_timestamp, 1, 1) ON CONFLICT (email) DO NOTHING;
