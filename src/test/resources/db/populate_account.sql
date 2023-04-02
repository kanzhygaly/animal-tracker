INSERT INTO account(first_name, last_name, email, password)
VALUES('John', 'Smith', 'john.smith@gmail.com', '$2a$10$XXLLzDHTuY472dzOx6NDyea0V0MhBRbw3nIxeYJ.OqUReNBmXymim')
ON CONFLICT (email) DO NOTHING;
INSERT INTO account(first_name, last_name, email, password)
VALUES('Johnson', 'Smith', 'johnyboy@gmail.com', '$2a$10$XXLLzDHTuY472dzOx6NDyea0V0MhBRbw3nIxeYJ.OqUReNBmXymim')
ON CONFLICT (email) DO NOTHING;
INSERT INTO account(first_name, last_name, email, password)
VALUES('John', 'Smith', 'john_smith@gmail.com', '$2a$10$XXLLzDHTuY472dzOx6NDyea0V0MhBRbw3nIxeYJ.OqUReNBmXymim')
ON CONFLICT (email) DO NOTHING;
INSERT INTO account(first_name, last_name, email, password)
VALUES('Abraham', 'Lincoln', 'alincoln@gmail.com', 'DummyPassword') ON CONFLICT (email) DO NOTHING;
