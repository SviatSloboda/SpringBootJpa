package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.springbootjdbcapi.dao.GroupDAO;
import ua.foxminded.springbootjdbcapi.dao.implementation.rowmapper.GroupRowMapper;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupDaoImpl implements GroupDAO {
    private final JdbcTemplate jdbcTemplate;
    private final GroupRowMapper groupRowMapper = new GroupRowMapper();

    public GroupDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int deleteById(int id) {
        String sqlQuery = """
                UPDATE public.students
                SET group_id = null
                WHERE group_id = ?;
                DELETE FROM public.groups
                WHERE group_id = ?;
                """;

        try {
            return jdbcTemplate.update(sqlQuery, id, id);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int update(Group object) {
        String sqlQuery = """
                UPDATE public.groups
                SET group_name = ?
                WHERE group_id = ?;
                """;

        try {
            return jdbcTemplate.update(sqlQuery, object.groupName(), object.id());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public List<Group> getAll() {
        String sql = """
                SELECT groups.group_id, groups.group_name
                FROM public.groups;
                """;

        try {
            return jdbcTemplate.query(sql, groupRowMapper);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Group> getById(int id) {
        String sql = """
                SELECT groups.group_id, groups.group_name
                FROM public.groups
                WHERE group_id = ?;
                """;

        try {
            return jdbcTemplate.query(sql, groupRowMapper, id)
                    .stream()
                    .findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(Group object) {
        String sql = """
                INSERT INTO public.groups (group_id, group_name)
                VALUES (?,?);
                """;

        try {
            return jdbcTemplate.update(sql, object.id(), object.groupName());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int saveWithoutId(Group object) {
        String sql = """
                INSERT INTO public.groups (group_name)
                VALUES (?);
                """;

        try {
            return jdbcTemplate.update(sql, object.groupName());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public List<Group> findAllGroupsWithLessOrEqualStudentsNumber(int studentNumber) {
        String sql = """
                SELECT groups.group_id, groups.group_name
                FROM public.groups
                LEFT JOIN public.students on students.group_id = groups.group_id
                GROUP BY groups.group_id
                HAVING COUNT(students.student_id) < ?;
                """;

        try {
            return jdbcTemplate.query(sql, groupRowMapper, studentNumber);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }
}
