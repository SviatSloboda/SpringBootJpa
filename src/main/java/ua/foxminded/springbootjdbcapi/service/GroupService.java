package ua.foxminded.springbootjdbcapi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.repository.GroupRepository;
import ua.foxminded.springbootjdbcapi.repository.StudentRepository;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public boolean save(Group group) {
        groupRepository.save(group);

        boolean exists = groupRepository.existsById(group.getId());
        if (!exists) {
            throw new IllegalStateException("Group was not created!");
        }

        return true;
    }

    @Transactional
    public boolean deleteAll() {
        List<Group> groups = groupRepository.findAll();
        List<Student> students = studentRepository.findAll();

        if (groups.isEmpty()) {
            throw new NoSuchElementException("No groups to delete");
        }

        for (Group group : groups) {
            group.getStudents().clear();
            groupRepository.save(group);
        }

        for (Student student : students) {
            student.setGroup(null);
            studentRepository.save(student);
        }

        groupRepository.deleteAll();

        boolean allDeleted = groupRepository.findAll().isEmpty();
        if (!allDeleted) {
            throw new IllegalStateException("Groups were not deleted");
        }

        return true;
    }

    @Transactional
    public boolean update(Group group) {
        if (!groupRepository.existsById(group.getId())) {
            throw new NoSuchElementException("Group with ID " + group.getId() + " does not exist.");
        }

        groupRepository.save(group);

        boolean updated = groupRepository.existsById(group.getId());
        if (!updated) {
            throw new IllegalStateException("Failed to update group with ID " + group.getId() + ".");
        }

        return true;
    }

    @Transactional
    public boolean deleteById(String id) {
        if (!groupRepository.existsById(id)) {
            throw new NoSuchElementException("Group with ID " + id + " does not exist.");
        }
        Optional<Group> optionalGroup = groupRepository.findById(id);

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();

            Iterator<Student> iterator = group.getStudents().iterator();

            while (iterator.hasNext()) {
                Student student = iterator.next();
                iterator.remove();
                student.setGroup(null);
            }

            groupRepository.deleteById(id);
            boolean deleted = !groupRepository.existsById(id);

            if (!deleted) {
                throw new IllegalStateException("Failed to delete group with ID " + id + ".");
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean existsById(String id) {
        return groupRepository.existsById(id);
    }

    public List<Group> getAllGroups() {
        List<Group> groups = groupRepository.findAll();

        if (groups.isEmpty()) {
            throw new NoSuchElementException("No groups were found");
        }

        return groups;
    }

    public Group getById(String id) {
        Optional<Group> group = groupRepository.findById(id);

        if (group.isEmpty()) {
            throw new NoSuchElementException("There is no such group with ID: " + id);
        }

        return group.get();
    }

    public List<String> getAllIds() {
        return groupRepository.findAll().stream().map(Group::getId).toList();
    }

    @Transactional
    public List<Group> findAllGroupsWithLessOrEqualsStudentCount(int studentCount) {
        if (studentCount < 0) {
            throw new IllegalArgumentException("Student count cannot be less than 0!");
        }

        List<Group> result = groupRepository.findAll().stream().filter(group -> group.getStudents().size() <= studentCount).toList();

        if (result.isEmpty()) {
            throw new NoSuchElementException("There are no such groups with less or equals student count: " + studentCount);
        }

        return result;
    }
}
