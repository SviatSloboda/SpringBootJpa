INSERT INTO groups (group_name)
VALUES
    ('Group A'),
    ('Group B');

INSERT INTO courses (course_name, course_description)
VALUES
    ('Math', 'Intro to math'),
    ('History', 'Intro to history'),
    ('Computer Science', 'Intro to programming');

INSERT INTO students (group_id, first_name, last_name)
VALUES
    (1, 'John', 'Doe'),
    (1, 'Alice', 'Smith'),
    (2, 'Bob', 'Johnson'),
    (1, 'Eva', 'Brown');

INSERT INTO student_courses (student_id, course_id)
VALUES
    (1, 1),
    (1, 3),
    (2, 1),
    (3, 2),
    (4, 3);

