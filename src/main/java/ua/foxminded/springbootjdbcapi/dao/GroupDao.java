package ua.foxminded.springbootjdbcapi.dao;

import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.List;

public interface GroupDao extends Dao<Group> {
    List<Group> findAllGroupsWithLessOrEqualStudentsNumber(int studentNumber);
}
