package ua.foxminded.springbootjdbcapi.dao.implementation.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getInt("group_id"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}
