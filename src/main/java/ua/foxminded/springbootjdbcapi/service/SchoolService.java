package ua.foxminded.springbootjdbcapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;

import java.util.NoSuchElementException;

@Service
public class SchoolService {
    private final StudentDao studentDao;
    private final StudentCourses studentCourses;
    private final CourseDao courseDao;

    @Autowired
    public SchoolService(StudentDao studentDao, StudentCourses studentCourses, CourseDao courseDao, GroupDao groupDao) {
        this.studentDao = studentDao;
        this.studentCourses = studentCourses;
        this.courseDao = courseDao;
    }

    public boolean deleteAll(){
        int result = studentCourses.deleteAll();

        if(result <= 0){
            throw new IllegalStateException("Students and Courses were not unpaired");
        }

        return true;
    }

    public boolean addStudentToCourse(int studentId, int courseId) {
        if (!studentDao.existsById(studentId)) {
            throw new NoSuchElementException("Student with ID " + studentId + " does not exist.");
        }

        if (!courseDao.existsById(courseId)) {
            throw new NoSuchElementException("Course with ID " + courseId + " does not exist.");
        }

        if (studentCourses.studentEnrolledOnCourse(studentId, courseId)) {
            throw new IllegalArgumentException("Student with ID " + studentId +
                                               " is already enrolled in course with ID " + courseId + ".");
        }

        int result = studentCourses.addStudentToCourse(studentId, courseId);
        if (result <= 0) {
            throw new IllegalStateException("Failed to enroll student with ID " + studentId +
                                            " in course with ID " + courseId + ".");
        }

        return true;
    }

    public boolean removeStudentFromCourse(int studentId, int courseId) {
        if (!studentDao.existsById(studentId)) {
            throw new NoSuchElementException("Student with ID " + studentId + " does not exist.");
        }

        if (!courseDao.existsById(courseId)) {
            throw new NoSuchElementException("Course with ID " + courseId + " does not exist.");
        }

        if (!studentCourses.studentEnrolledOnCourse(studentId, courseId)) {
            throw new IllegalArgumentException("Student with ID " + studentId +
                                               " is not enrolled in course with ID " + courseId + ".");
        }

        int result = studentCourses.removeStudentFromCourse(studentId, courseId);
        if (result <= 0) {
            throw new IllegalStateException("Failed to delete student with ID " + studentId +
                                            " from course with ID " + courseId + ".");
        }

        return true;
    }

    public boolean studentEnrolledOnCourse(int studentId, int courseId){
        if (!studentDao.existsById(studentId)) {
            throw new NoSuchElementException("Student with ID " + studentId + " does not exist.");
        }

        if (!courseDao.existsById(courseId)) {
            throw new NoSuchElementException("Course with ID " + courseId + " does not exist.");
        }

        return studentCourses.studentEnrolledOnCourse(studentId, courseId);
    }
}
