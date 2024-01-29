package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.CourseDAO;
import ua.foxminded.springbootjdbcapi.dao.implementation.rowmapper.CourseRowMapper;
import ua.foxminded.springbootjdbcapi.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseDaoImpl implements CourseDAO {
    private final JdbcTemplate jdbcTemplate;
    private final CourseRowMapper courseRowMapper = new CourseRowMapper();

    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int deleteById(int id) {
        String sql = """
                   DELETE FROM public.student_courses
                   WHERE course_id = ?;
                   DELETE FROM public.courses
                   WHERE course_id = ?;
                """;

        try {
            return jdbcTemplate.update(sql, id, id);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int update(Course object) {
        String sql = """
                   UPDATE public.courses
                   SET course_name = ?, course_description = ?
                   WHERE course_id = ?
                """;

        try {
            return jdbcTemplate.update(sql, object.name(), object.description(), object.id());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public List<Course> getAll() {
        String sql = """
                   SELECT course_id, course_name, course_description
                   FROM public.courses
                   LIMIT 100;
                """;

        try {
            return jdbcTemplate.query(sql, courseRowMapper);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Course> getById(int id) {
        String sql = """
                   SELECT course_id, course_name, course_description
                   FROM public.courses
                   WHERE course_id = ?;
                """;

        try {
            return jdbcTemplate.query(sql, courseRowMapper, id).stream().findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(Course object) {
        String sql = """
                   INSERT INTO public.courses(course_id, course_name, course_description) 
                   values (?,?,?);
                """;

        try {
            return jdbcTemplate.update(sql, object.id(), object.name(), object.description());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int saveWithoutId(Course object) {
        String sql = """
                   INSERT INTO public.courses(course_name, course_description) 
                   values (?,?);
                """;

        try {
            return jdbcTemplate.update(sql, object.name(), object.description());
        } catch (DataAccessException e) {
            return 0;
        }
    }
}
