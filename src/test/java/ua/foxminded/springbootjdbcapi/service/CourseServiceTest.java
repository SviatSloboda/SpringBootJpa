package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CourseService.class})
class CourseServiceTest {
    @MockBean
    CourseDao courseDao;

    @Autowired
    CourseService courseService;

    @Test
    void shouldCreateNewCourseWithoutId() {
        // Arrange
        Course course = new Course("test", "test");
        when(courseDao.saveWithoutId(any(Course.class))).thenReturn(1);

        // Act
        boolean result = courseService.createCourseWithoutId(course);

        // Assert
        assertTrue(result);
        verify(courseDao).saveWithoutId(any(Course.class));
    }

    @Test
    void shouldThrowIllegalStateException_whenNewCourseWithoutIdWasNotCreated() {
        // Arrange
        Course course = new Course("test", "test");
        when(courseDao.saveWithoutId(course)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> courseService.createCourseWithoutId(course));
    }

    @Test
    void createCourse() {
        // Arrange
        Course course = new Course(1, "test", "test");
        when(courseDao.save(course)).thenReturn(1);

        // Act
        boolean result = courseService.createCourse(course);

        // Assert
        assertTrue(result);
        verify(courseDao).save(any(Course.class));
    }

    @Test
    void shouldThrowIllegalStateException_whenNewCourseWasNotCreated() {
        // Arrange
        Course course = new Course(1, "test", "test");
        when(courseDao.save(course)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> courseService.createCourse(course));
    }

    @Test
    void shouldDeleteCourseById() {
        // Arrange
        int id = 1;
        when(courseDao.existsById(id)).thenReturn(true);
        when(courseDao.deleteById(id)).thenReturn(1);

        // Act
        boolean result = courseService.deleteCourseById(id);

        // Assert
        assertTrue(result);
        verify(courseDao).existsById(id);
        verify(courseDao).deleteById(id);
    }

    @Test
    void shouldDeleteAllCourses() {
        // Arrange
        when(courseDao.deleteAll()).thenReturn(10);

        // Act
        boolean result = courseService.deleteAll();

        // Assert
        assertTrue(result);
        verify(courseDao).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllCoursesWereNotDeleted() {
        // Arrange
        when(courseDao.deleteAll()).thenReturn(0);

        // Act
        assertThrows(IllegalStateException.class,
                () -> courseService.deleteAll(),
                "Courses were not deleted");
    }

    @Test
    void shouldThrowNoSuchElementException_whenCourseNotExistsByDeleting() {
        // Arrange
        int id = 1;
        when(courseDao.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> courseService.deleteCourseById(id));
    }

    @Test
    void shouldThrowIllegalStateException_whenCourseWasNotDeleted() {
        // Arrange
        int id = 1;
        when(courseDao.existsById(id)).thenReturn(true);
        when(courseDao.deleteById(id)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> courseService.deleteCourseById(id));
    }

    @Test
    void updateCourse() {
        // Arrange
        Course course = new Course(1, "test", "test");
        when(courseDao.existsById(course.id())).thenReturn(true);
        when(courseDao.update(course)).thenReturn(1);

        // Act
        boolean result = courseService.updateCourse(course);

        // Assert
        assertTrue(result);
        verify(courseDao).update(course);
    }

    @Test
    void shouldThrowNoSuchElementException_whenCourseNotExistsByUpdating() {
        // Arrange
        Course course = new Course(1, "test", "test");
        when(courseDao.existsById(course.id())).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> courseService.updateCourse(course));
    }

    @Test
    void shouldThrowIllegalStateException_whenCourseWasNotUpdated() {
        // Arrange
        Course course = new Course(1, "test", "test");
        when(courseDao.existsById(course.id())).thenReturn(true);
        when(courseDao.update(course)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> courseService.updateCourse(course));
    }

    @Test
    void shouldBeTrue_whenCourseExistsById() {
        // Arrange
        int id = 1;
        when(courseDao.existsById(id)).thenReturn(true);

        // Act
        boolean result = courseService.courseExistsById(id);

        // Assert
        assertTrue(result);
        verify(courseDao).existsById(id);
    }

    @Test
    void shouldBeFalse_whenCourseDoesNotExistById() {
        // Arrange
        int id = 1;
        when(courseDao.existsById(id)).thenReturn(false);

        // Act
        boolean result = courseService.courseExistsById(id);

        // Assert
        assertFalse(result);
        verify(courseDao).existsById(id);
    }

    @Test
    void shouldGetAllCourses() {
        // Arrange
        List<Course> expected = new ArrayList<>(List.of(new Course("test", "test")));
        when(courseDao.getAll()).thenReturn(expected);

        // Act
        List<Course> actual = courseService.getAllCourses();

        // Assert
        assertEquals(expected, actual);
        verify(courseDao).getAll();
    }

    @Test
    void shouldThrowNoSuchElementException_whenNoCoursesFound() {
        // Arrange
        List<Course> expected = new ArrayList<>();
        when(courseDao.getAll()).thenReturn(expected);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> courseService.getAllCourses());
    }

    @Test
    void shouldGetCourseById() {
        // Arrange
        int id = 1;
        Course expectedCourse = new Course(1, "test", "test");
        when(courseDao.getById(id)).thenReturn(Optional.of(expectedCourse));

        // Act
        Course actualCourse = courseService.getCourseById(id);

        // Assert
        assertEquals(expectedCourse, actualCourse);
        verify(courseDao).getById(id);
    }

    @Test
    void shouldThrowNoSuchElementException_whenCourseByIdNotFound() {
        // Arrange
        int id = 1;
        when(courseDao.getById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> courseService.getCourseById(id));
    }
}
