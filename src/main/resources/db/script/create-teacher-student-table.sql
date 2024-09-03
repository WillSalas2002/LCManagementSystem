CREATE TABLE IF NOT EXISTS teacher_student
(
    student_id INT,
    teacher_id INT,
    FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE SET NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id) ON DELETE SET NULL
);