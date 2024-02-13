package ua.foxminded.springbootjdbcapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.model.Course;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseDao courseDao;

    @Autowired
    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public boolean deleteAll() {
        boolean wereDeleted = courseDao.deleteAll();

        if (!wereDeleted) {
            throw new IllegalStateException("Courses were not deleted");
        }

        return true;
    }

    public boolean save(Course course) {
        boolean wasCreated = courseDao.save(course);

        if (!wasCreated) {
            throw new IllegalStateException("Course was not created!");
        }

        return true;
    }

    public boolean deleteById(String id) {
        if (!courseDao.existsById(id)) {
            throw new NoSuchElementException("Course with ID " + id + " does not exist.");
        }

        boolean wasDeleted = courseDao.deleteById(id);
        if (!wasDeleted) {
            throw new IllegalStateException("Failed to delete course with ID " + id + ".");
        }

        return true;
    }

    public boolean update(Course course) {
        if (!courseDao.existsById(course.getId())) {
            throw new NoSuchElementException("Course with ID " + course.getId() + " does not exist.");
        }

        boolean wasUpdated = courseDao.update(course);
        if (!wasUpdated) {
            throw new IllegalStateException("Failed to update course with ID " + course.getId() + ".");
        }

        return true;
    }

    public boolean existsById(String id) {
        return courseDao.existsById(id);
    }

    public List<Course> getAllCourses() {
        List<Course> courses = courseDao.getAll();

        if (courses.isEmpty()) {
            throw new NoSuchElementException("No courses were found");
        }

        return courses;
    }

    public Course getById(String id) {
        Optional<Course> course = courseDao.getById(id);

        if (course.isEmpty()) {
            throw new NoSuchElementException("There is no such course with ID: " + id);
        }

        return course.get();
    }
}
