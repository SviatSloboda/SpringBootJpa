DROP TABLE IF EXISTS student_courses CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE IF NOT EXISTS groups
(
    group_id   VARCHAR(255) PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS students
(
    student_id VARCHAR(255) PRIMARY KEY,
    group_id   VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE IF NOT EXISTS courses
(
    course_id          VARCHAR(255) PRIMARY KEY,
    course_name        VARCHAR(255) NOT NULL,
    course_description TEXT
);

CREATE TABLE IF NOT EXISTS student_courses
(
    student_id VARCHAR(255),
    course_id  VARCHAR(255),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students (student_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);