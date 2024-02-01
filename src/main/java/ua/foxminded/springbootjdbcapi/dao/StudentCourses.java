package ua.foxminded.springbootjdbcapi.dao;

import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;

public interface StudentCourses {
    int addStudentToCourse(int studentId, int courseId);

    int removeStudentFromCourse(int studentId, int courseId);
}
