INSERT INTO account(first_name, last_name, email, password)
VALUES('John', 'Smith', 'john.smith@gmail.com', 'DummyPassword') ON CONFLICT (email) DO NOTHING;
