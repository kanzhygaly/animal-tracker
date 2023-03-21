INSERT INTO visited_location(animal_id, location_id, visited_date_time)
VALUES(1, 1, current_timestamp) ON CONFLICT (animal_id, location_id) DO NOTHING;
INSERT INTO visited_location(animal_id, location_id, visited_date_time)
VALUES(1, 2, current_timestamp) ON CONFLICT (animal_id, location_id) DO NOTHING;
