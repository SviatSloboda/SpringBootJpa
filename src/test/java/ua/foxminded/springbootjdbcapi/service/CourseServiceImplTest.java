package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.springbootjdbcapi.service.CourseService;
import ua.foxminded.springbootjdbcapi.model.Course;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        CourseService.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/drop_tables.sql", "/sql/create_tables.sql", "/sql/insert_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Testcontainers
class CourseServiceImplTest {

    @Autowired
    private CourseService courseService;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenCourseIsRemoved() {
        // Given
        // When
        courseService.deleteById("1");

        // Then
        assertThrows(NoSuchElementException.class, () -> courseService.getById("1"), "There is no such course with ID: 1");
    }

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenRowsAffectedIs1() {
        // Given
        // When
        boolean wasDeleted = courseService.deleteById("1");

        // Then
        assertTrue(wasDeleted);
    }

    @Test
    void modify_GivenCourseWithNewNameAndDescription_WhenUpdated_ThenCourseIsUpdated() {
        // Given
        Course newCourse = new Course("1", "test", "test");

        // When
        courseService.update(newCourse);

        // Then
        assertEquals(newCourse.getName(), courseService.getById("1").getName());
        assertEquals(newCourse.getDescription(), courseService.getById("1").getDescription());
    }

    @Test
    void modify_GivenCourse_WhenUpdated_ThenRowsAffectedIs1() {
        // Given
        Course newCourse = new Course("1", "test", "test");

        // When
        boolean wasSaved = courseService.update(newCourse);

        // Then
        assertTrue(wasSaved);
    }

    @Test
    void fetchAll_WhenCalled_ThenCourseNamesMatch() {
        // Given
        List<String> coursesNames = List.of(
                "Math", "History", "Computer Science"
        );

        // When
        List<String> actual = courseService.getAllCourses().stream()
                .map(Course::getName)
                .toList();

        // Then
        assertEquals(coursesNames, actual);
    }

    @Test
    void fetchById_GivenNewlyCreatedCourse_WhenFetched_ThenReturnNewCourse() {
        // Given
        Course expected = new Course("33", "test", "test");
        courseService.save(expected);

        // When
        Course actual = courseService.getById("33");

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void fetchById_GivenId1_WhenFetched_ThenReturnCourseWithId1() {
        // Given
        Course expected = new Course("1", "Math", "Intro to math");

        // When
        Course actual = courseService.getById("1");

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void store_GivenNewCourse_WhenSaved_ThenRetrieveThisCourse() {
        // Given
        Course newCourse = new Course("101", "test", "test");

        // When
        courseService.save(newCourse);

        // Then
        assertEquals(newCourse, courseService.getById("101"));
    }

    @Test
    void store_GivenNewCourse_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Course newCourse = new Course("101", "test", "test");

        // When
        boolean wasSaved = courseService.save(newCourse);

        // Then
        assertTrue(wasSaved);
    }

    @Test
    void existsById_GivenExistingId_WhenChecked_ThenReturnsTrue() {
        // Given
        String existingId = "1";

        // When
        boolean exists = courseService.existsById(existingId);

        // Then
        assertTrue(exists);
    }

    @Test
    void existsById_GivenNonExistingId_WhenChecked_ThenReturnsFalse() {
        // Given
        String nonExistingId = "999";

        // When
        boolean exists = courseService.existsById(nonExistingId);

        // Then
        assertFalse(exists);
    }

    @Test
    void deleteAll_WhenCalled_ThenAllCoursesAreRemoved() {
        // Given

        // When
        courseService.deleteAll();
        assertThrows(NoSuchElementException.class, () -> courseService.getAllCourses(), "No courses were found");
    }

    @Test
    void getAllIds_WhenCalled_ThenReturnsListOfAllIds() {
        // Given
        List<String> expectedIds = List.of("1", "2", "3");

        // When
        List<String> actualIds = courseService.getAllIds();

        // Then
        assertNotNull(actualIds);
        assertFalse(actualIds.isEmpty());
        assertEquals(expectedIds.size(), actualIds.size());
        assertTrue(actualIds.containsAll(expectedIds));
    }

}
