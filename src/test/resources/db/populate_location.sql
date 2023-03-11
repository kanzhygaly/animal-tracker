INSERT INTO location(latitude, longitude) VALUES(1.23, 2.23) ON CONFLICT (latitude, longitude) DO NOTHING;
INSERT INTO location(latitude, longitude) VALUES(1.34, 1.54) ON CONFLICT (latitude, longitude) DO NOTHING;
