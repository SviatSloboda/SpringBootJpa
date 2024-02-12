package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SchoolService.class)
class SchoolServiceTest {
    @MockBean
    CourseDao courseDao;

    @MockBean
    GroupDao groupDao;

    @MockBean
    StudentDao studentDao;

    @MockBean
    StudentCourses studentCourses;

    @Autowired
    SchoolService schoolService;

    private final String studentId = "1";
    private final String courseId = "1";

    @Test
    void shouldAddStudentToCourseSuccessfully() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);
        when(studentCourses.addStudentToCourse(studentId, courseId)).thenReturn(true);

        // Act
        boolean result = schoolService.addStudentToCourse(studentId, courseId);

        // Assert
        assertTrue(result);
        verify(studentCourses).addStudentToCourse(studentId, courseId);
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfStudentDoesNotExistWhenAddingToCourse() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldDeleteAllPairsOfStudentsAndCourses() {
        // Arrange
        when(studentCourses.deleteAll()).thenReturn(true);

        // Act
        boolean result = schoolService.deleteAll();

        // Assert
        assertTrue(result);
        verify(studentCourses).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllPairsOfStudentsAndCoursesWereNotUnpaired() {
        // Arrange
        when(studentCourses.deleteAll()).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> schoolService.deleteAll(),
                "Students and Courses were not unpaired");
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfCourseDoesNotExistWhenAddingStudentToCourse() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfStudentAlreadyEnrolledInCourse() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalStateExceptionIfAddingStudentToCourseFails() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);
        when(studentCourses.addStudentToCourse(studentId, courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldRemoveStudentFromCourseSuccessfully() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);
        when(studentCourses.removeStudentFromCourse(studentId, courseId)).thenReturn(true);

        // Act
        boolean result = schoolService.removeStudentFromCourse(studentId, courseId);

        // Assert
        assertTrue(result);
        verify(studentCourses).removeStudentFromCourse(studentId, courseId);
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfStudentDoesNotExistWhenRemovingFromCourse() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfCourseDoesNotExistWhenRemovingStudentFromCourse() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfStudentNotEnrolledInCourseWhenRemoving() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalStateExceptionIfRemovingStudentFromCourseFails() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);
        when(studentCourses.removeStudentFromCourse(studentId, courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldVerifyStudentIsEnrolledOnCourse() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);

        // Act
        boolean result = schoolService.studentEnrolledOnCourse(studentId, courseId);

        // Assert
        assertTrue(result);
        verify(studentCourses).studentEnrolledOnCourse(studentId, courseId);
    }

    @Test
    void shouldVerifyStudentIsNotEnrolledOnCourse() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);

        // Act
        boolean result = schoolService.studentEnrolledOnCourse(studentId, courseId);

        // Assert
        assertFalse(result);
        verify(studentCourses).studentEnrolledOnCourse(studentId, courseId);
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfStudentDoesNotExistWhenCheckingEnrollment() {
        // Arrange
        when(studentDao.existsById(studentId)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> schoolService.studentEnrolledOnCourse(studentId, courseId));
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfCourseDoesNotExistWhenCheckingEnrollment() {
        // Arrange
        when(courseDao.existsById(courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> schoolService.studentEnrolledOnCourse(studentId, courseId));
    }
}
