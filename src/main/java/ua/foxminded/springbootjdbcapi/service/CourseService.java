package ua.foxminded.springbootjdbcapi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.repository.CourseRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public boolean save(Course course) {
        courseRepository.save(course);

        boolean exists = courseRepository.existsById(course.getId());
        if (!exists) {
            throw new IllegalStateException("Course was not created!");
        }

        return true;
    }

    @Transactional
    public boolean deleteAll() {
        courseRepository.deleteAll();

        return courseRepository.findAll().isEmpty();
    }

    @Transactional
    public boolean update(Course course) {
        if (!courseRepository.existsById(course.getId())) {
            throw new NoSuchElementException("Course with ID " + course.getId() + " does not exist.");
        }
        courseRepository.save(course);
        boolean updated = courseRepository.existsById(course.getId());
        if (!updated) {
            throw new IllegalStateException("Failed to update course with ID " + course.getId() + ".");
        }
        return true;
    }

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

    public boolean existsById(String id) {
        return courseRepository.existsById(id);
    }

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            throw new NoSuchElementException("No courses were found");
        }
        return courses;
    }

    public Course getById(String id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new NoSuchElementException("There is no such course with ID: " + id);
        }
        return course.get();
    }

    public List<String> getAllIds(){
        return courseRepository.findAll().stream().map(Course::getId).toList();
    }
}
