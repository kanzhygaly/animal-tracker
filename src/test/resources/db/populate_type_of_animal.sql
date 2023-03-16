INSERT INTO animal_animal_type(animal_id, type_id) VALUES(1, 1) ON CONFLICT (animal_id, type_id) DO NOTHING;
INSERT INTO animal_animal_type(animal_id, type_id) VALUES(1, 2) ON CONFLICT (animal_id, type_id) DO NOTHING;
