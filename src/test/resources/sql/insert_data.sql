INSERT INTO groups (group_id, group_name)
VALUES
    ('1', 'Group A'),
    ('2', 'Group B');

INSERT INTO courses (course_id, course_name, course_description)
VALUES
    ('1', 'Math', 'Intro to math'),
    ('2', 'History', 'Intro to history'),
    ('3', 'Computer Science', 'Intro to programming');

INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES
    ('1', '1', 'John', 'Doe'),
    ('2', '1', 'Alice', 'Smith'),
    ('3', '2', 'Bob', 'Johnson'),
    ('4', '1', 'Eva', 'Brown');

INSERT INTO student_courses (student_id, course_id)
VALUES
    ('1', '1'),
    ('1', '3'),
    ('2', '1'),
    ('3', '2'),
    ('4', '3');
