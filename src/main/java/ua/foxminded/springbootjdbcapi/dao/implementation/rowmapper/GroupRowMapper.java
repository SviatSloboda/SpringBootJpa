package ua.foxminded.springbootjdbcapi.dao.implementation.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Group(
                rs.getInt("group_id"),
                rs.getString("group_name")
        );
    }
}
