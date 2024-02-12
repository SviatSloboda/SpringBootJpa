package ua.foxminded.springbootjdbcapi.dao.implementation;

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
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.model.Course;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        CourseDao.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/drop_tables.sql", "/sql/create_tables.sql", "/sql/insert_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Testcontainers
class CourseDaoImplTest {

    @Autowired
    private CourseDao courseDao;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenCourseIsRemoved() {
        // Given
        // When
        courseDao.deleteById("1");

        // Then
        assertEquals(Optional.empty(), courseDao.getById("1"));
    }

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenRowsAffectedIs1() {
        // Given
        // When
        boolean wasDeleted = courseDao.deleteById("1");

        // Then
        assertTrue(wasDeleted);
    }

    @Test
    void modify_GivenCourseWithNewNameAndDescription_WhenUpdated_ThenCourseIsUpdated() {
        // Given
        Course newCourse = new Course("1", "test", "test");

        // When
        courseDao.update(newCourse);

        // Then
        assertEquals(newCourse.getName(), courseDao.getById("1").get().getName());
        assertEquals(newCourse.getDescription(), courseDao.getById("1").get().getDescription());
    }

    @Test
    void modify_GivenCourse_WhenUpdated_ThenRowsAffectedIs1() {
        // Given
        Course newCourse = new Course("1", "test", "test");

        // When
        boolean wasSaved = courseDao.update(newCourse);

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
        List<String> actual = courseDao.getAll().stream()
                .map(Course::getName)
                .toList();

        // Then
        assertEquals(coursesNames, actual);
    }

    @Test
    void fetchById_GivenNewlyCreatedCourse_WhenFetched_ThenReturnNewCourse() {
        // Given
        Course expected = new Course("33", "test", "test");
        courseDao.save(expected);

        // When
        Course actual = courseDao.getById("33").get();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void fetchById_GivenId1_WhenFetched_ThenReturnCourseWithId1() {
        // Given
        Course expected = new Course("1", "Math", "Intro to math");

        // When
        Course actual = courseDao.getById("1").get();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void store_GivenNewCourse_WhenSaved_ThenRetrieveThisCourse() {
        // Given
        Course newCourse = new Course("101", "test", "test");

        // When
        courseDao.save(newCourse);

        // Then
        assertEquals(newCourse, courseDao.getById("101").get());
    }

    @Test
    void store_GivenNewCourse_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Course newCourse = new Course("101", "test", "test");

        // When
        boolean wasSaved = courseDao.save(newCourse);

        // Then
        assertTrue(wasSaved);
    }
}
