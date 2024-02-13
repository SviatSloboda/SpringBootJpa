package ua.foxminded.springbootjdbcapi.dao;

public interface StudentCourses {
    boolean addStudentToCourse(String studentId, String courseId);

    boolean removeStudentFromCourse(String studentId, String courseId);

    boolean studentEnrolledOnCourse(String studentId, String courseId);
    boolean deleteAll();
}
