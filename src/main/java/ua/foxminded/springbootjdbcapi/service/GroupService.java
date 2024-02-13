package ua.foxminded.springbootjdbcapi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupDao groupDao;

    @Autowired
    public GroupService(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Transactional
    public boolean save(Group group) {
        boolean wasCreated = groupDao.save(group);

        if (!wasCreated) {
            throw new IllegalStateException("Group was not created!");
        }

        return true;
    }

    public boolean deleteAll() {
        boolean wereDeleted = groupDao.deleteAll();

        if (!wereDeleted) {
            throw new IllegalStateException("Groups were not deleted");
        }

        return true;
    }

    public boolean update(Group group) {
        if (!groupDao.existsById(group.getId())) {
            throw new NoSuchElementException("Group with ID " + group.getId() + " does not exist.");
        }

        boolean wasUpdated = groupDao.update(group);
        if (!wasUpdated) {
            throw new IllegalStateException("Failed to update group with ID " + group.getId() + ".");
        }

        return true;
    }


    public boolean deleteById(String id) {
        if (!groupDao.existsById(id)) {
            throw new NoSuchElementException("Group with ID " + id + " does not exist.");
        }

        boolean wasDeleted = groupDao.deleteById(id);
        if (!wasDeleted) {
            throw new IllegalStateException("Failed to delete group with ID " + id + ".");
        }

        return true;
    }

    public boolean existsById(String id) {
        return groupDao.existsById(id);
    }

    public List<Group> getAllGroups() {
        List<Group> groups = groupDao.getAll();

        if (groups.isEmpty()) {
            throw new NoSuchElementException("No groups were found");
        }

        return groups;
    }

    public Group getById(String id) {
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
        if (result.isEmpty()) {
            throw new NoSuchElementException("There are no such courses with less or equals student count: " + studentCount);
        }

        return result;
    }
}
