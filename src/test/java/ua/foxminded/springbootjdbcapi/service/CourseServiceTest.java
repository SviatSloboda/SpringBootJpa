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

    private final Course course = new Course("1", "test", "test");

    @Test
    void createCourse() {
        // Arrange
        when(courseDao.save(course)).thenReturn(true);

        // Act
        boolean result = courseService.save(course);

        // Assert
        assertTrue(result);
        verify(courseDao).save(any(Course.class));
    }

    @Test
    void shouldThrowIllegalStateException_whenNewCourseWasNotCreated() {
        // Arrange
        when(courseDao.save(course)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> courseService.save(course));
    }

    @Test
    void shouldRemoveCourseById() {
        // Arrange
        String id = "1";
        when(courseDao.existsById(id)).thenReturn(true);
        when(courseDao.deleteById(id)).thenReturn(true);

        // Act
        boolean result = courseService.deleteById(id);

        // Assert
        assertTrue(result);
        verify(courseDao).existsById(id);
        verify(courseDao).deleteById(id);
    }

    @Test
    void shouldRemoveAllCourses() {
        // Arrange
        when(courseDao.deleteAll()).thenReturn(true);

        // Act
        boolean result = courseService.deleteAll();

        // Assert
        assertTrue(result);
        verify(courseDao).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllCoursesWereNotDeleted() {
        // Arrange
        when(courseDao.deleteAll()).thenReturn(false);

        // Act
        assertThrows(IllegalStateException.class,
                () -> courseService.deleteAll(),
                "Courses were not deleted");
    }

    @Test
    void shouldThrowNoSuchElementException_whenCourseNotExistsByDeleting() {
        // Arrange
        String id = "1";
        when(courseDao.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> courseService.deleteById(id));
    }

    @Test
    void shouldThrowIllegalStateException_whenCourseWasNotDeleted() {
        // Arrange
        String id = "1";
        when(courseDao.existsById(id)).thenReturn(true);
        when(courseDao.deleteById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> courseService.deleteById(id));
    }

    @Test
    void modifyCourse() {
        // Arrange
        when(courseDao.existsById(course.getId())).thenReturn(true);
        when(courseDao.update(course)).thenReturn(true);

        // Act
        boolean result = courseService.update(course);

        // Assert
        assertTrue(result);
        verify(courseDao).update(course);
    }

    @Test
    void shouldThrowNoSuchElementException_whenCourseNotExistsByUpdating() {
        // Arrange
        when(courseDao.existsById(course.getId())).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> courseService.update(course));
    }

    @Test
    void shouldThrowIllegalStateException_whenCourseWasNotUpdated() {
        // Arrange
        when(courseDao.existsById(course.getId())).thenReturn(true);
        when(courseDao.update(course)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> courseService.update(course));
    }

    @Test
    void shouldBeTrue_whenCourseExistsById() {
        // Arrange
        String id = "1";
        when(courseDao.existsById(id)).thenReturn(true);

        // Act
        boolean result = courseService.existsById(id);

        // Assert
        assertTrue(result);
        verify(courseDao).existsById(id);
    }

    @Test
    void shouldBeFalse_whenCourseDoesNotExistById() {
        // Arrange
        String id = "1";
        when(courseDao.existsById(id)).thenReturn(false);

        // Act
        boolean result = courseService.existsById(id);

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
        String id = "1";
        Course expectedCourse = new Course("1", "test", "test");
        when(courseDao.getById(id)).thenReturn(Optional.of(expectedCourse));

        // Act
        Course actualCourse = courseService.getById(id);

        // Assert
        assertEquals(expectedCourse, actualCourse);
        verify(courseDao).getById(id);
    }

    @Test
    void shouldThrowNoSuchElementException_whenCourseByIdNotFound() {
        // Arrange
        String id = "1";
        when(courseDao.getById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> courseService.getById(id));
    }
}
