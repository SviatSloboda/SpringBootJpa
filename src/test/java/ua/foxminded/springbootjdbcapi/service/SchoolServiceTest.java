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

    @Test
    void shouldAddStudentToCourseSuccessfully() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);
        when(studentCourses.addStudentToCourse(studentId, courseId)).thenReturn(1);

        boolean result = schoolService.addStudentToCourse(studentId, courseId);

        assertTrue(result);
        verify(studentCourses).addStudentToCourse(studentId, courseId);
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfStudentDoesNotExistWhenAddingToCourse() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldDeleteAllPairsOfStudentsAndCourses() {
        // Arrange
        when(studentCourses.deleteAll()).thenReturn(10);

        // Act
        boolean result = schoolService.deleteAll();

        // Assert
        assertTrue(result);
        verify(studentCourses).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllPairsOfStudentsAndCoursesWereNotUnpaired() {
        // Arrange
        when(studentCourses.deleteAll()).thenReturn(0);

        // Act
        assertThrows(IllegalStateException.class,
                () -> schoolService.deleteAll(),
                "Students and Courses were not unpaired");
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfCourseDoesNotExistWhenAddingStudentToCourse() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfStudentAlreadyEnrolledInCourse() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalStateExceptionIfAddingStudentToCourseFails() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);
        when(studentCourses.addStudentToCourse(studentId, courseId)).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> schoolService.addStudentToCourse(studentId, courseId));
    }

    @Test
    void shouldRemoveStudentFromCourseSuccessfully() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);
        when(studentCourses.removeStudentFromCourse(studentId, courseId)).thenReturn(1);

        boolean result = schoolService.removeStudentFromCourse(studentId, courseId);

        assertTrue(result);
        verify(studentCourses).removeStudentFromCourse(studentId, courseId);
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfStudentDoesNotExistWhenRemovingFromCourse() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfCourseDoesNotExistWhenRemovingStudentFromCourse() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfStudentNotEnrolledInCourseWhenRemoving() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldThrowIllegalStateExceptionIfRemovingStudentFromCourseFails() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);
        when(studentCourses.removeStudentFromCourse(studentId, courseId)).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> schoolService.removeStudentFromCourse(studentId, courseId));
    }

    @Test
    void shouldVerifyStudentIsEnrolledOnCourse() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(true);

        assertTrue(schoolService.studentEnrolledOnCourse(studentId, courseId));
        verify(studentCourses).studentEnrolledOnCourse(studentId, courseId);
    }

    @Test
    void shouldVerifyStudentIsNotEnrolledOnCourse() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(true);
        when(courseDao.existsById(courseId)).thenReturn(true);
        when(studentCourses.studentEnrolledOnCourse(studentId, courseId)).thenReturn(false);

        assertFalse(schoolService.studentEnrolledOnCourse(studentId, courseId));
        verify(studentCourses).studentEnrolledOnCourse(studentId, courseId);
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfStudentDoesNotExistWhenCheckingEnrollment() {
        int studentId = 1;
        int courseId = 1;

        when(studentDao.existsById(studentId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> schoolService.studentEnrolledOnCourse(studentId, courseId));
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfCourseDoesNotExistWhenCheckingEnrollment() {
        int studentId = 1;
        int courseId = 1;

        when(courseDao.existsById(courseId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> schoolService.studentEnrolledOnCourse(studentId, courseId));
    }
}
