CREATE TABLE IF NOT EXISTS teacher
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR(100)         NOT NULL,
    last_name   VARCHAR(100)         NOT NULL,
    middle_name VARCHAR(100)         NOT NULL,
    age         INT CHECK (age > 15) NOT NULL,
    course_id   INT,
    FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE SET NULL,
    CONSTRAINT UC_Teacher UNIQUE (first_name, last_name, middle_name)
);