package ua.foxminded.springbootjdbcapi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.repository.CourseRepository;
import ua.foxminded.springbootjdbcapi.repository.GroupRepository;
import ua.foxminded.springbootjdbcapi.repository.StudentRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public boolean saveStudentWithOwnId(Student student){
        if(getAllIds().contains(student.getId())){
            throw new IllegalStateException("This id is already used. Create another one!");
        }

        return save(student);
    }

    @Transactional
    public boolean save(Student student) {
        studentRepository.save(student);

        boolean exists = studentRepository.existsById(student.getId());
        if (!exists) {
            throw new IllegalStateException("Student was not created!");
        }

        return true;
    }

    @Transactional
    public boolean deleteAll() {
        List<Student> students = studentRepository.findAll();
        List<Group> groups = groupRepository.findAll();
        List<Course> courses = courseRepository.findAll();

        if (students.isEmpty()) {
            throw new NoSuchElementException("No students to delete");
        }

        for (Group group : groups) {
            group.getStudents().clear();
            groupRepository.save(group);
        }

        for (Student student : students) {
            student.setGroup(null);
            studentRepository.save(student);
        }

        for(Course course: courses){
            course.setStudents(null);
            courseRepository.save(course);
        }

        studentRepository.deleteAll();

        boolean allDeleted = studentRepository.findAll().isEmpty();
        if (!allDeleted) {
            throw new IllegalStateException("Students were not deleted");
        }

        return true;
    }

    @Transactional
    public boolean update(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new NoSuchElementException("Student with ID " + student.getId() + " does not exist.");
        }

        studentRepository.save(student);

        boolean updated = studentRepository.existsById(student.getId());
        if (!updated) {
            throw new IllegalStateException("Failed to update student with ID " + student.getId() + ".");
        }

        return true;
    }

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

    public boolean existsById(String id) {
        return studentRepository.existsById(id);
    }

    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            throw new NoSuchElementException("No students were found");
        }
        return students;
    }

    public Student getById(String id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new NoSuchElementException("There is no such student with ID: " + id);
        }
        return student.get();
    }

    public List<String> getAllIds(){
        return studentRepository.findAll().stream().map(Student::getId).toList();
    }

    @Transactional
    public List<Student> findAllStudentsByCourseName(String courseName) {
        List<Student> students = studentRepository.findAll().stream()
                .filter(student -> student.getCourses()
                        .stream().map(Course::getName).toList()
                        .contains(courseName))
                .toList();

        if (students.isEmpty()) throw new NoSuchElementException("No students were found!");

        return students;
    }
}