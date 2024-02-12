package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.*;

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
    private final Student student = new Student("1", new Group("1", "test"), "John", "Doe");
    private final Course course = new Course("1", "test", "test");

    @Test
    void save() {
        // Arrange
        when(studentDao.save(student)).thenReturn(true);

        // Act
        boolean result = studentService.save(student);

        // Assert
        assertTrue(result);
        verify(studentDao).save(any(Student.class));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNewStudentWasNotCreated() {
        // Arrange
        when(studentDao.save(student)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> studentService.save(student));
    }

    @Test
    void shouldDeleteStudentById() {
        // Arrange
        String id = "1";
        when(studentDao.existsById(id)).thenReturn(true);
        when(studentDao.deleteById(id)).thenReturn(true);

        // Act
        boolean result = studentService.deleteById(id);

        // Assert
        assertTrue(result);
        verify(studentDao).deleteById(id);
    }

    @Test
    void shouldDeleteAllStudents() {
        // Arrange
        when(studentDao.deleteAll()).thenReturn(true);

        // Act
        boolean result = studentService.deleteAll();

        // Assert
        assertTrue(result);
        verify(studentDao).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllStudentsWereNotDeleted() {
        // Arrange
        when(studentDao.deleteAll()).thenReturn(false);

        // Act
        assertThrows(IllegalStateException.class,
                () -> studentService.deleteAll(),
                "Students were not deleted");
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenStudentNotExistsByDeleting() {
        // Arrange
        String id = "1";
        when(studentDao.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.deleteById(id));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenStudentWasNotDeleted() {
        // Arrange
        String id = "1";
        when(studentDao.existsById(id)).thenReturn(true);
        when(studentDao.deleteById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> studentService.deleteById(id));
    }

    @Test
    void update() {
        // Arrange
        when(studentDao.existsById(student.getId())).thenReturn(true);
        when(studentDao.update(student)).thenReturn(true);

        // Act
        boolean result = studentService.update(student);

        // Assert
        assertTrue(result);
        verify(studentDao).update(student);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenStudentNotExistsByUpdating() {
        // Arrange
        when(studentDao.existsById(student.getId())).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.update(student));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenStudentWasNotUpdated() {
        // Arrange
        when(studentDao.existsById(student.getId())).thenReturn(true);
        when(studentDao.update(student)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> studentService.update(student));
    }

    @Test
    void shouldBeTrueWhenStudentExistsById() {
        // Arrange
        String id = "1";
        when(studentDao.existsById(id)).thenReturn(true);

        // Act
        boolean result = studentService.existsById(id);

        // Assert
        assertTrue(result);
        verify(studentDao).existsById(id);
    }

    @Test
    void shouldBeFalseWhenStudentExistsById() {
        // Arrange
        String id = "1";
        when(studentDao.existsById(id)).thenReturn(false);

        // Act
        boolean result = studentService.existsById(id);

        // Assert
        assertFalse(result);
        verify(studentDao).existsById(id);
    }

    @Test
    void shouldGetAllStudents() {
        // Arrange
        List<Student> expected = new ArrayList<>(List.of(new Student("1", new Group("1", "test"), "John", "Doe")));
        when(studentDao.getAll()).thenReturn(expected);

        // Act
        List<Student> actual = studentService.getAll();

        // Assert
        assertEquals(expected, actual);
        verify(studentDao).getAll();
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNoStudentsWereReturned() {
        // Arrange
        when(studentDao.getAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.getAll());
    }

    @Test
    void shouldGetStudentById() {
        // Arrange
        String id = "1";
        Optional<Student> expected = Optional.of(new Student("1", new Group("1", "test"), "John", "Doe"));
        when(studentDao.getById(id)).thenReturn(expected);

        // Act
        Student actual = studentService.getById(id);

        // Assert
        assertEquals(expected.get(), actual);
        verify(studentDao).getById(id);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNoStudentWasReturned() {
        // Arrange
        String id = "1";
        when(studentDao.getById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.getById(id));
    }

    @Test
    void shouldFindAllStudentsByCourseName() {
        // Arrange
        List<Student> expected = new ArrayList<>(
                List.of(new Student("1", new Group("1", "test"), "John", "Doe"))
        );

        // Act
        when(studentDao.findAllStudentsByCourseName(course.getName())).thenReturn(expected);
        when(courseDao.getAll()).thenReturn(List.of(course));

        List<Student> actual = studentService.findAllStudentsByCourseName(course.getName());

        // Assert
        assertEquals(expected, actual);
        verify(studentDao).findAllStudentsByCourseName(any(String.class));
    }

    @Test
    void shouldThrowNoSuchElementException_whenNoStudentsFoundForCourseName() {
        // Arrange
        String nonExistentCourseName = "Nonexistent Course";
        when(studentDao.findAllStudentsByCourseName(nonExistentCourseName)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            studentService.findAllStudentsByCourseName(nonExistentCourseName);
        });
    }
}
