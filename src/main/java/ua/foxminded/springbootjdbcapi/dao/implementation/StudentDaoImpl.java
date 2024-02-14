package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentDaoImpl implements StudentDao {

    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            for (Course course : student.getCourses()) {
                course.getStudents().remove(student);
            }

            studentRepository.deleteById(id);

            return !studentRepository.existsById(id);
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(Student object) {
        if(!studentRepository.existsById(object.getId())) return false;


        studentRepository.save(object);

        return studentRepository.existsById(object.getId());
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        studentRepository.deleteAll();

        return studentRepository.findAll().isEmpty();
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean save(Student object) {
        studentRepository.save(object);

        return studentRepository.existsById(object.getId());
    }

    @Override
    public boolean existsById(String id) {
        return studentRepository.existsById(id);
    }

    @Override
    public List<String> getAllIds() {
        return studentRepository.findAll().stream().map(Student::getId).toList();
    }

    @Override
    @Transactional
    public List<Student> findAllStudentsByCourseName(String courseName) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getCourses()
                        .stream().map(Course::getName).toList()
                        .contains(courseName))
                .toList();
    }
}

