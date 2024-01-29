package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.springbootjdbcapi.dao.StudentDAO;
import ua.foxminded.springbootjdbcapi.dao.implementation.rowmapper.StudentRowMapper;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDAO {
    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper = new StudentRowMapper();

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int deleteById(int id) {
        String sqlQuery = """
                DELETE FROM public.student_courses
                WHERE student_id = ?;
                DELETE FROM public.students
                WHERE student_id = ?;
                """;

        try {
            return jdbcTemplate.update(sqlQuery, id, id);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int update(Student object) {
        String sqlQuery = """
                UPDATE public.students
                SET group_id = ?, first_name = ?, last_name = ?
                WHERE student_id = ?
                """;

        try {
            return jdbcTemplate.update(sqlQuery, object.groupId(),
                    object.firstName(),
                    object.lastName(),
                    object.id()
            );
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public List<Student> getAll() {
        String sql = """
                SELECT students.student_id, students.group_id,
                students.first_name, students.last_name
                FROM public.students;
                """;

        try {
            return jdbcTemplate.query(sql, studentRowMapper);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Student> getById(int id) {
        String sql = """
                SELECT students.student_id, students.group_id,
                students.first_name, students.last_name
                FROM public.students
                WHERE student_id = ?;
                """;

        try {
            return jdbcTemplate.query(sql, studentRowMapper, id)
                    .stream()
                    .filter(student -> student.id() == id)
                    .findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(Student object) {
        String sql = """
                INSERT INTO public.students (student_id, group_id, first_name, last_name) 
                VALUES (?,?,?,?);
                """;

        try {
            return jdbcTemplate.update(sql, object.id(), object.groupId(), object.firstName(), object.lastName());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int saveWithoutId(Student object) {
        String sql = """
                INSERT INTO public.students (group_id, first_name, last_name)
                VALUES (?,?,?);
                """;

        try {
            return jdbcTemplate.update(sql, object.groupId(), object.firstName(), object.lastName());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public List<Student> findAllStudentsByCourseName(String courseName) {
        String sql = """
                SELECT students.student_id, students.group_id,
                students.first_name, students.last_name
                FROM public.students
                INNER JOIN public.student_courses ON students.student_id = student_courses.student_id
                INNER JOIN public.courses ON courses.course_id = student_courses.course_id
                WHERE courses.course_name LIKE ?;
                """;

        try {
            return jdbcTemplate.query(sql, studentRowMapper, courseName);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }
}
