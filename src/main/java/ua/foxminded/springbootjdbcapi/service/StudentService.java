package ua.foxminded.springbootjdbcapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    @Autowired
    public StudentService(StudentDao studentDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    public boolean createStudentWithoutId(Student student) {
        int result = studentDao.saveWithoutId(student);

        if (result <= 0) {
            throw new IllegalStateException("Student was not created!");
        }

        return true;
    }

    public boolean createStudent(Student student) {
        int result = studentDao.save(student);

        if (result <= 0) {
            throw new IllegalStateException("Student was not created!");
        }

        return true;
    }

    public boolean deleteStudentById(int id) {
        if (!studentDao.existsById(id)) {
            throw new NoSuchElementException("Student with ID " + id + " does not exist.");
        }

        int result = studentDao.deleteById(id);
        if (result <= 0) {
            throw new IllegalStateException("Failed to delete student with ID " + id + ".");
        }

        return true;
    }

    public boolean deleteAll(){
        int result = studentDao.deleteAll();

        if(result <= 0){
            throw new IllegalStateException("Students were not deleted");
        }

        return true;
    }

    public boolean updateStudent(Student student) {
        if (!studentDao.existsById(student.id())) {
            throw new NoSuchElementException("Student with ID " + student.id() + " does not exist.");
        }

        int result = studentDao.update(student);
        if (result <= 0) {
            throw new IllegalStateException("Failed to update student with ID " + student.id() + ".");
        }

        return true;
    }

    public boolean studentExistsById(int id) {
        return studentDao.existsById(id);
    }

    public List<Student> getAllStudents() {
        List<Student> students = studentDao.getAll();

        if (students.isEmpty()) {
            throw new NoSuchElementException("No students were found");
        }

        return students;
    }

    public Student getStudentById(int id) {
        Optional<Student> student = studentDao.getById(id);

        if (student.isEmpty()) {
            throw new NoSuchElementException("There is no such student with ID: " + id);
        }

        return student.get();
    }

    public List<Student> findAllStudentsByCourseName(String courseName) {
        courseDao.getAll().stream()
                .filter(course -> course.name().equals(courseName))
                .findAny().orElseThrow(() -> new NoSuchElementException("There is no such course with the name: " + courseName));

        List<Student> result = studentDao.findAllStudentsByCourseName(courseName);
        if (result.size() <= 0) {
            throw new NoSuchElementException("No students were found!");
        }

        return result;
    }
}
