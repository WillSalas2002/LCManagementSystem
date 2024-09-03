CREATE TABLE IF NOT EXISTS student
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR(100)        NOT NULL,
    last_name   VARCHAR(100)        NOT NULL,
    middle_name VARCHAR(100)        NOT NULL,
    age         INT CHECK (age > 5) NOT NULL,
    CONSTRAINT UC_Student UNIQUE (first_name, last_name, middle_name)
);