INSERT INTO account(first_name, last_name, email, password)
VALUES('John', 'Smith', 'john.smith@gmail.com', 'DummyPassword') ON CONFLICT (email) DO NOTHING;
INSERT INTO account(first_name, last_name, email, password)
VALUES('Johnson', 'Smith', 'johnyboy@gmail.com', 'DummyPassword') ON CONFLICT (email) DO NOTHING;
INSERT INTO account(first_name, last_name, email, password)
VALUES('John', 'Smith', 'john_smith@gmail.com', 'DummyPassword') ON CONFLICT (email) DO NOTHING;
INSERT INTO account(first_name, last_name, email, password)
VALUES('Abraham', 'Lincoln', 'alincoln@gmail.com', 'DummyPassword') ON CONFLICT (email) DO NOTHING;
