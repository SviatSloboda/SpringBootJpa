package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;

@Repository
public class StudentCoursesImpl implements StudentCourses {
    private final JdbcTemplate jdbcTemplate;

    public StudentCoursesImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int deleteAll() {
        int result = 0;

        result += jdbcTemplate.update("DELETE FROM public.student_courses");

        result += jdbcTemplate.update("UPDATE public.students SET group_id = null");

        result += jdbcTemplate.update("DELETE FROM public.students");
        result += jdbcTemplate.update("DELETE FROM public.groups");
        result += jdbcTemplate.update("DELETE FROM public.courses");

        if (result > 0) {
            jdbcTemplate.execute("ALTER SEQUENCE students_student_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE groups_group_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE courses_course_id_seq RESTART WITH 1");
        }

        return result;
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
            System.out.println("Error adding student to course " + e);
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
