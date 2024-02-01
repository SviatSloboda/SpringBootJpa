package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = StudentService.class)
public class StudentServiceTest {
    @MockBean
    StudentDao studentDao;

    @MockBean
    CourseDao courseDao;

    @Autowired
    StudentService studentService;

    @Test
    void shouldCreateNewStudentWithoutId() {
        // Arrange
        Student student = new Student(1,"John", "Doe");
        when(studentDao.saveWithoutId(any(Student.class))).thenReturn(1);

        // Act
        boolean result = studentService.createStudentWithoutId(student);

        // Assert
        assertTrue(result);
        verify(studentDao).saveWithoutId(any(Student.class));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNewStudentWithoutIdWasNotCreated() {
        // Arrange
        Student student = new Student(1,"John", "Doe");
        when(studentDao.saveWithoutId(student)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> studentService.createStudentWithoutId(student));
    }

    @Test
    void createStudent() {
        // Arrange
        Student student = new Student(1,1, "John", "Doe");
        when(studentDao.save(student)).thenReturn(1);

        // Act
        boolean result = studentService.createStudent(student);

        // Assert
        assertTrue(result);
        verify(studentDao).save(any(Student.class));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNewStudentWasNotCreated() {
        // Arrange
        Student student = new Student(1,1, "John", "Doe");
        when(studentDao.save(student)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> studentService.createStudent(student));
    }

    @Test
    void shouldDeleteStudentById() {
        // Arrange
        int id = 1;
        when(studentDao.existsById(id)).thenReturn(true);
        when(studentDao.deleteById(id)).thenReturn(1);

        // Act
        boolean result = studentService.deleteStudentById(id);

        // Assert
        assertTrue(result);
        verify(studentDao).deleteById(id);
    }

    @Test
    void shouldDeleteAllStudents() {
        // Arrange
        when(studentDao.deleteAll()).thenReturn(10);

        // Act
        boolean result = studentService.deleteAll();

        // Assert
        assertTrue(result);
        verify(studentDao).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllStudentsWereNotDeleted() {
        // Arrange
        when(studentDao.deleteAll()).thenReturn(0);

        // Act
        assertThrows(IllegalStateException.class,
                () -> studentService.deleteAll(),
                "Students were not deleted");
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenStudentNotExistsByDeleting() {
        // Arrange
        int id = 1;
        when(studentDao.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.deleteStudentById(id));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenStudentWasNotDeleted() {
        // Arrange
        int id = 1;
        when(studentDao.existsById(id)).thenReturn(true);
        when(studentDao.deleteById(id)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> studentService.deleteStudentById(id));
    }

    @Test
    void updateStudent() {
        // Arrange
        Student student = new Student(1, "John", "Doe");
        when(studentDao.existsById(student.id())).thenReturn(true);
        when(studentDao.update(student)).thenReturn(1);

        // Act
        boolean result = studentService.updateStudent(student);

        // Assert
        assertTrue(result);
        verify(studentDao).update(student);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenStudentNotExistsByUpdating() {
        // Arrange
        Student student = new Student(1, "John", "Doe");
        when(studentDao.existsById(student.id())).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.updateStudent(student));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenStudentWasNotUpdated() {
        // Arrange
        Student student = new Student(1, "John", "Doe");
        when(studentDao.existsById(student.id())).thenReturn(true);
        when(studentDao.update(student)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> studentService.updateStudent(student));
    }

    @Test
    void shouldBeTrueWhenStudentExistsById() {
        // Arrange
        int id = 1;
        when(studentDao.existsById(id)).thenReturn(true);

        // Act
        boolean result = studentService.studentExistsById(id);

        // Assert
        assertTrue(result);
        verify(studentDao).existsById(id);
    }

    @Test
    void shouldBeFalseWhenStudentExistsById() {
        // Arrange
        int id = 1;
        when(studentDao.existsById(id)).thenReturn(false);

        // Act
        boolean result = studentService.studentExistsById(id);

        // Assert
        assertFalse(result);
        verify(studentDao).existsById(id);
    }

    @Test
    void shouldGetAllStudents() {
        // Arrange
        List<Student> expected = new ArrayList<>(List.of(new Student(1, "John", "Doe")));
        when(studentDao.getAll()).thenReturn(expected);

        // Act
        List<Student> actual = studentService.getAllStudents();

        // Assert
        assertEquals(expected, actual);
        verify(studentDao).getAll();
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNoStudentsWereReturned() {
        // Arrange
        when(studentDao.getAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.getAllStudents());
    }

    @Test
    void shouldGetStudentById() {
        // Arrange
        int id = 1;
        Optional<Student> expected = Optional.of(new Student(id, "John", "Doe"));
        when(studentDao.getById(id)).thenReturn(expected);

        // Act
        Student actual = studentService.getStudentById(id);

        // Assert
        assertEquals(expected.get(), actual);
        verify(studentDao).getById(id);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNoStudentWasReturned() {
        // Arrange
        int id = 1;
        when(studentDao.getById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.getStudentById(id));
    }

    @Test
    void shouldFindAllStudentsByCourseName() {
        // Arrange
        Course course = new Course(1, "test", "test");

        List<Student> expected = new ArrayList<>(
                List.of(new Student(1, 1, "test", "test"))
        );

        // Act
        when(studentDao.findAllStudentsByCourseName(course.name())).thenReturn(expected);
        when(courseDao.getAll()).thenReturn(List.of(course));

        List<Student> actual = studentService.findAllStudentsByCourseName(course.name());

        // Assert
        assertEquals(expected, actual);
        verify(studentDao).findAllStudentsByCourseName(any(String.class));
    }

    @Test
    void shouldThrowNoSuchElementException_whenCourseNotExistsByFindAllStudentsByCourseName() {
        // Arrange
        Course course = new Course(1, "test", "test");

        List<Student> expected = new ArrayList<>(
                List.of(new Student(1, 1, "test", "test"))
        );

        when(studentDao.findAllStudentsByCourseName(course.name())).thenReturn(expected);
        when(courseDao.getAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> studentService.findAllStudentsByCourseName(course.name()),
                "No students were found!");
    }
}