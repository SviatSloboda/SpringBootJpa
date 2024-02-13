package ua.foxminded.springbootjdbcapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentDao studentDao;

    @Autowired
    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public boolean save(Student student) {
        boolean wasSaved = studentDao.save(student);

        if (!wasSaved) {
            throw new IllegalStateException("Student was not created!");
        }

        return true;
    }

    public boolean deleteById(String id) {
        if (!studentDao.existsById(id)) {
            throw new NoSuchElementException("Student with ID " + id + " does not exist.");
        }

        boolean wasDeleted = studentDao.deleteById(id);
        if (!wasDeleted) {
            throw new IllegalStateException("Failed to delete student with ID " + id + ".");
        }

        return true;
    }

    public boolean deleteAll() {
        boolean wereDeleted = studentDao.deleteAll();

        if (!wereDeleted) {
            throw new IllegalStateException("Students were not deleted");
        }

        return true;
    }

    public boolean update(Student student) {
        if (!studentDao.existsById(student.getId())) {
            throw new NoSuchElementException("Student with ID " + student.getId() + " does not exist.");
        }

        boolean wasUpdated = studentDao.update(student);
        if (!wasUpdated) {
            throw new IllegalStateException("Failed to update student with ID " + student.getId() + ".");
        }

        return true;
    }

    public boolean existsById(String id) {
        return studentDao.existsById(id);
    }

    public List<Student> getAll() {
        List<Student> students = studentDao.getAll();

        if (students.isEmpty()) {
            throw new NoSuchElementException("No students were found");
        }

        return students;
    }

    public Student getById(String id) {
        Optional<Student> student = studentDao.getById(id);

        if (student.isEmpty()) {
            throw new NoSuchElementException("There is no such student with ID: " + id);
        }

        return student.get();
    }

    public List<Student> findAllStudentsByCourseName(String courseName) {
        List<Student> students = studentDao.findAllStudentsByCourseName(courseName);

        if (students.isEmpty()) throw new NoSuchElementException("No students were found!");

        return students;
    }
}
