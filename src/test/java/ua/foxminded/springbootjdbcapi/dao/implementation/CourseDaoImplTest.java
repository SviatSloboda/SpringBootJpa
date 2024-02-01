package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.springbootjdbcapi.model.Course;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/drop_tables.sql"})
@Sql(scripts = {"/sql/create_tables.sql"})
@Sql(scripts = {"/sql/insert_data.sql"})
@ActiveProfiles("test")
@Testcontainers
class CourseDaoImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CourseDaoImpl courseDao;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        courseDao = new CourseDaoImpl(jdbcTemplate);
    }

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenCourseIsRemoved() {
        // Given
        // When
        courseDao.deleteById(1);

        // Then
        assertEquals(Optional.empty(), courseDao.getById(1));
    }

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenRowsAffectedIs1() {
        // Given
        // When
        int rowsAffected = courseDao.deleteById(1);

        // Then
        assertEquals(2, rowsAffected);
    }

    @Test
    void update_GivenCourseWithNewNameAndDescription_WhenUpdated_ThenCourseIsUpdated() {
        // Given
        Course newCourse = new Course(1, "test", "test");

        // When
        courseDao.update(newCourse);

        // Then
        assertEquals(newCourse.name(), courseDao.getById(1).get().name());
        assertEquals(newCourse.description(), courseDao.getById(1).get().description());
    }

    @Test
    void update_GivenCourse_WhenUpdated_ThenRowsAffectedIs1() {
        // Given
        Course newCourse = new Course(1, "test", "test");

        // When
        int rowsAffected = courseDao.update(newCourse);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void getAll_WhenCalled_ThenCourseNamesMatch() {
        // Given
        List<String> coursesNames = List.of(
                "Math", "History", "Computer Science"
        );

        // When
        List<String> actual = courseDao.getAll().stream()
                .map(Course::name)
                .toList();

        // Then
        assertEquals(coursesNames, actual);
    }

    @Test
    void getById_GivenNewlyCreatedCourse_WhenFetched_ThenReturnNewCourse() {
        // Given
        Course expected = new Course(33, "test", "test");
        courseDao.save(expected);

        // When
        Course actual = courseDao.getById(33).get();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void getById_GivenId1_WhenFetched_ThenReturnCourseWithId1() {
        // Given
        Course expected = new Course(1, "Math", "Intro to math");

        // When
        Course actual = courseDao.getById(1).get();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void save_GivenNewCourse_WhenSaved_ThenRetrieveThisCourse() {
        // Given
        Course newCourse = new Course(101, "test", "test");

        // When
        courseDao.save(newCourse);

        // Then
        assertEquals(newCourse, courseDao.getById(101).get());
    }

    @Test
    void save_GivenNewCourse_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Course newCourse = new Course(101, "test", "test");

        // When
        int rowsAffected = courseDao.save(newCourse);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void saveWithoutId_GivenCourseWithoutId_WhenSaved_ThenCanBeRetrieved() {
        // Given
        Course createCourse = new Course("Geography", "map");
        courseDao.saveWithoutId(createCourse);

        // When
        Course actualCourse = courseDao.getAll().stream()
                .max(Comparator.comparingInt(Course::id))
                .orElseThrow(() -> new NoSuchElementException("No courses found"));

        // Then
        Course expectedCourse = new Course(actualCourse.id(), createCourse.name(), createCourse.description());
        assertEquals(expectedCourse, actualCourse, "Retrieved course should match the saved one");
    }

    @Test
    void saveWithoutId_GivenNewCourse_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Course newCourse = new Course("test", "test");

        // When
        int rowsAffected = courseDao.save(newCourse);

        // Then
        assertEquals(1, rowsAffected);
    }
}
