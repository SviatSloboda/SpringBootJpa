package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;

public class StudentCoursesImpl implements StudentCourses {
    private final JdbcTemplate jdbcTemplate;

    public StudentCoursesImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addStudentToCourse(int studentId, int courseId) {
        String sql = """
                INSERT INTO public.student_courses (student_id, course_id) 
                VALUES (?,?); 
                """;

        try {
            return jdbcTemplate.update(sql, studentId, courseId);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int removeStudentFromCourse(int studentId, int courseId) {
        String sql = """
                DELETE FROM student_courses
                WHERE student_id = ? AND course_id = ?;
                """;

        try {
            return jdbcTemplate.update(sql, studentId, courseId);
        } catch (DataAccessException e) {
            return 0;
        }
    }
}
