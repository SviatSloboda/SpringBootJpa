package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.repository.CourseRepository;
import ua.foxminded.springbootjdbcapi.repository.StudentRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentCoursesImpl implements StudentCourses {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public boolean addStudentToCourse(String studentId, String courseId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (studentOptional.isEmpty() || courseOptional.isEmpty()) {
            return false;
        }

        Course course = courseOptional.get();
        Student student = studentOptional.get();

        course.getStudents().add(student);
        student.getCourses().add(course);

        return true;
    }

    @Override
    @Transactional
    public boolean removeStudentFromCourse(String studentId, String courseId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (courseOptional.isEmpty() || studentOptional.isEmpty()) {
            return false;
        }

        Course course = courseOptional.get();
        Student student = studentOptional.get();

        course.getStudents().remove(student);
        student.getCourses().remove(course);

        return true;
    }



    @Override
    public boolean studentEnrolledOnCourse(String studentId, String courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Course> course = courseRepository.findById(courseId);

        if(student.isEmpty() || course.isEmpty()) return false;

        return  course.get().getStudents().contains(student.get());
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        return true;
    }
}