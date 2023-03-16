INSERT INTO animal_location(animal_id, location_id) VALUES(1, 1) ON CONFLICT (animal_id, location_id) DO NOTHING;
INSERT INTO animal_location(animal_id, location_id) VALUES(1, 2) ON CONFLICT (animal_id, location_id) DO NOTHING;
