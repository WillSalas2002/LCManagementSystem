CREATE TABLE IF NOT EXISTS student_course
(
    student_id INT,
    course_id  INT,
    FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE SET NULL,
    FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE SET NULL
);