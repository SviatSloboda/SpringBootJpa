package ua.foxminded.springbootjdbcapi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.repository.CourseRepository;
import ua.foxminded.springbootjdbcapi.repository.StudentRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SchoolService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SchoolService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public boolean addStudentToCourse(String studentId, String courseId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty())
            throw new NoSuchElementException("Student with ID " + studentId + " does not exist.");

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty())
            throw new NoSuchElementException("Course with ID " + courseId + " does not exist.");

        if (studentEnrolledOnCourse(studentId, courseId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " is already enrolled in course with ID " + courseId + ".");
        }

        Course course = courseOptional.get();
        Student student = studentOptional.get();

        course.getStudents().add(student);
        student.getCourses().add(course);

        studentRepository.flush();
        courseRepository.flush();

        if (!studentEnrolledOnCourse(studentId, courseId)) {
            throw new IllegalStateException("Failed to enroll student with ID " + studentId + " in course with ID " + courseId + ".");
        }

        return true;
    }

    @Transactional
    public boolean removeStudentFromCourse(String studentId, String courseId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty())
            throw new NoSuchElementException("Student with ID " + studentId + " does not exist.");

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty())
            throw new NoSuchElementException("Course with ID " + courseId + " does not exist.");

        if (!studentEnrolledOnCourse(studentId, courseId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " is not enrolled in course with ID " + courseId + ".");
        }


        Course course = courseOptional.get();
        Student student = studentOptional.get();

        course.getStudents().remove(student);
        student.getCourses().remove(course);

        studentRepository.flush();
        courseRepository.flush();

        if (studentEnrolledOnCourse(studentId, courseId)) {
            throw new IllegalStateException("Failed to delete student with ID " + studentId + " from course with ID " + courseId + ".");
        }

        return true;
    }


    public boolean studentEnrolledOnCourse(String studentId, String courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty())
            throw new NoSuchElementException("Student with ID " + studentId + " does not exist.");

        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty())
            throw new NoSuchElementException("Course with ID " + courseId + " does not exist.");


        return course.get().getStudents().contains(student.get());
    }

    @Transactional
    public void deleteAll() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        if (!studentRepository.findAll().isEmpty() || !courseRepository.findAll().isEmpty()) {
            throw new IllegalStateException("Students and Courses were not unpaired");
        }
    }


}
