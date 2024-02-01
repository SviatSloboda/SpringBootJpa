package ua.foxminded.springbootjdbcapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GroupService {
    GroupDao groupDao;

    @Autowired
    public GroupService(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public boolean createGroupWithoutId(Group group) {
        int result = groupDao.saveWithoutId(group);

        if (result <= 0) {
            throw new IllegalStateException("Group was not created!");
        }

        return true;
    }

    public boolean deleteAll(){
        int result = groupDao.deleteAll();

        if(result <= 0){
            throw new IllegalStateException("Groups were not deleted");
        }

        return true;
    }

    public boolean createGroup(Group group) {
        int result = groupDao.save(group);

        if (result <= 0) {
            throw new IllegalStateException("Group was not created!");
        }

        return true;
    }

    public boolean deleteGroupById(int id) {
        if (!groupDao.existsById(id)) {
            throw new NoSuchElementException("Group with ID " + id + " does not exist.");
        }

        int result = groupDao.deleteById(id);
        if (result <= 0) {
            throw new IllegalStateException("Failed to delete group with ID " + id + ".");
        }

        return true;
    }

    public boolean updateGroup(Group group) {
        if (!groupDao.existsById(group.id())) {
            throw new NoSuchElementException("Group with ID " + group.id() + " does not exist.");
        }

        int result = groupDao.update(group);
        if (result <= 0) {
            throw new IllegalStateException("Failed to update group with ID " + group.id() + ".");
        }

        return true;
    }

    public boolean groupExistsById(int id) {
        return groupDao.existsById(id);
    }

    public List<Group> getAllGroups() {
        List<Group> groups = groupDao.getAll();

        if (groups.isEmpty()) {
            throw new NoSuchElementException("No groups were found");
        }

        return groups;
    }

    public Group getGroupById(int id) {
        Optional<Group> group = groupDao.getById(id);

        if (group.isEmpty()) {
            throw new NoSuchElementException("There is no such group with ID: " + id);
        }

        return group.get();
    }

    public List<Group> findAllGroupsWithLessOrEqualsStudentCount(int studentCount) {
        if (studentCount < 0) {
            throw new IllegalArgumentException("Student count can not be less than 0!");
        }

        List<Group> result = groupDao.findAllGroupsWithLessOrEqualStudentsNumber(studentCount);
        if (result.size() <= 0) {
            throw new NoSuchElementException("There are no such courses with less or equals student count: " + studentCount);
        }

        return result;
    }
}
