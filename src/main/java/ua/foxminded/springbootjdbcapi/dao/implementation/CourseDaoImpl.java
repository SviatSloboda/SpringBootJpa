package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseDaoImpl implements CourseDao {

    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();

            for (Student student : course.getStudents()) {
                course.getStudents().remove(student);
            }

            courseRepository.deleteById(id);

            return !courseRepository.existsById(id);
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(Course object) {
        if(!courseRepository.existsById(object.getId())) return false;

        courseRepository.save(object);

        return courseRepository.existsById(object.getId());
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        courseRepository.deleteAll();

        return courseRepository.findAll().isEmpty();
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getById(String id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean save(Course object) {
        courseRepository.save(object);

        return courseRepository.existsById(object.getId());
    }

    @Override
    public boolean existsById(String id) {
        return courseRepository.existsById(id);
    }

    @Override
    public List<String> getAllIds() {
        return courseRepository.findAll().stream().map(Course::getId).toList();
    }
}

