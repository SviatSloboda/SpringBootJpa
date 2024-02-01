package ua.foxminded.springbootjdbcapi.dao;

public interface StudentCourses {
    int addStudentToCourse(int studentId, int courseId);

    int removeStudentFromCourse(int studentId, int courseId);

    boolean studentEnrolledOnCourse(int studentId, int courseId);

    int deleteAll();
}
