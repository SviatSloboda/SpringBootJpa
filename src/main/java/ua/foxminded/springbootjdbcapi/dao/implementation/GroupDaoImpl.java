package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.repository.GroupRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupDaoImpl implements GroupDao {

    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public boolean deleteById(String id) {
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

            return !groupRepository.existsById(id);
        } else {
            return false;
        }
    }


    @Transactional
    @Override
    public boolean update(Group object) {
        if(!groupRepository.existsById(object.getId())) return false;


        groupRepository.save(object);

        return groupRepository.existsById(object.getId());
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        List<Group> groups = groupRepository.findAll();
        for (Group group : groups) {
            group.getStudents().clear();
            groupRepository.save(group);
        }
        groupRepository.deleteAll();
        return groupRepository.findAll().isEmpty();
    }

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<Group> getById(String id) {
        return groupRepository.findById(id);
    }

    @Transactional
    @Override
    public boolean save(Group object) {
        groupRepository.save(object);

        return groupRepository.existsById(object.getId());
    }

    @Override
    public boolean existsById(String id) {
        return groupRepository.existsById(id);
    }

    @Override
    @Transactional
    public List<Group> findAllGroupsWithLessOrEqualStudentsNumber(int studentNumber) {
        return groupRepository.findAll().stream().filter(group -> group.getStudents().size() <= studentNumber).toList();
    }

    @Override
    public List<String> getAllIds() {
        return groupRepository.findAll().stream().map(Group::getId).toList();
    }
}
