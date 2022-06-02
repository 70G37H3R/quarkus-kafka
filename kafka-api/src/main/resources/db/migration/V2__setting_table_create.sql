CREATE TABLE setting
(
    id SERIAL PRIMARY KEY,
    timeout int,
    retries int,
    polling int

);
ALTER SEQUENCE setting_id_seq RESTART 10;