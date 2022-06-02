CREATE TABLE device
(
    id SERIAL PRIMARY KEY,
    name  TEXT NOT NULL,
    ipAddress TEXT NOT NULL,
    macAddress   TEXT NOT NULL,
    status      TEXT NOT NULL,
    type       TEXT NOT NULL,
    version       TEXT NOT NULL,
    CONSTRAINT ipAddress UNIQUE (ipAddress),
    CONSTRAINT macAddress UNIQUE (macAddress)

);
ALTER SEQUENCE device_id_seq RESTART 1000000;