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
    CourseDao courseDao;

    @Autowired
    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public boolean createCourseWithoutId(Course course) {
        int result = courseDao.saveWithoutId(course);

        if (result <= 0) {
            throw new IllegalStateException("Course was not created!");
        }

        return true;
    }

    public boolean deleteAll() {
        int result = courseDao.deleteAll();

        if (result <= 0) {
            throw new IllegalStateException("Courses were not deleted");
        }

        return true;
    }

    public boolean createCourse(Course course) {
        int result = courseDao.save(course);

        if (result <= 0) {
            throw new IllegalStateException("Course was not created!");
        }

        return true;
    }

    public boolean deleteCourseById(int id) {
        if (!courseDao.existsById(id)) {
            throw new NoSuchElementException("Course with ID " + id + " does not exist.");
        }

        int result = courseDao.deleteById(id);
        if (result <= 0) {
            throw new IllegalStateException("Failed to delete course with ID " + id + ".");
        }

        return true;
    }

    public boolean updateCourse(Course course) {
        if (!courseDao.existsById(course.id())) {
            throw new NoSuchElementException("Course with ID " + course.id() + " does not exist.");
        }

        int result = courseDao.update(course);
        if (result <= 0) {
            throw new IllegalStateException("Failed to update course with ID " + course.id() + ".");
        }

        return true;
    }

    public boolean courseExistsById(int id) {
        return courseDao.existsById(id);
    }

    public List<Course> getAllCourses() {
        List<Course> courses = courseDao.getAll();

        if (courses.isEmpty()) {
            throw new NoSuchElementException("No courses were found");
        }

        return courses;
    }

    public Course getCourseById(int id) {
        Optional<Course> course = courseDao.getById(id);

        if (course.isEmpty()) {
            throw new NoSuchElementException("There is no such course with ID: " + id);
        }

        return course.get();
    }
}
