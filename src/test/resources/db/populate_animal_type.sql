INSERT INTO animal_type(type_name) VALUES('cattle') ON CONFLICT (type_name) DO NOTHING;
INSERT INTO animal_type(type_name) VALUES('mammal') ON CONFLICT (type_name) DO NOTHING;
