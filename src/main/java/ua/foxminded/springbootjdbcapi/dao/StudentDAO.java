package ua.foxminded.springbootjdbcapi.dao;

import ua.foxminded.springbootjdbcapi.model.Student;


import java.util.List;

public interface StudentDAO extends DAO<Student> {
    List<Student> findAllStudentsByCourseName(String courseName);
}
