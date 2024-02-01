package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;

@Repository
public class StudentCoursesImpl implements StudentCourses {
    private final JdbcTemplate jdbcTemplate;

    public StudentCoursesImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int deleteAll(){
        String sql = """
                DELETE FROM public.student_courses;
                """;
        try {
            return jdbcTemplate.update(sql);
        } catch (DataAccessException e){
            return 0;
        }
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
                DELETE FROM public.student_courses
                WHERE student_id = ? AND course_id = ?;
                """;

        try {
            return jdbcTemplate.update(sql, studentId, courseId);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public boolean studentEnrolledOnCourse(int studentId, int courseId) {
        String sql = """
                SELECT COUNT(*) FROM public.student_courses
                WHERE student_courses.student_id = ? AND student_courses.course_id = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, studentId, courseId) > 0;
        } catch (DataAccessException e){
            return false;
        }
    }
}
