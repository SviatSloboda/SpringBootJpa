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
import ua.foxminded.springbootjdbcapi.model.Student;

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
class StudentCoursesImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentCoursesImpl studentCourses;
    private StudentDaoImpl studentDao;

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
        studentCourses = new StudentCoursesImpl(jdbcTemplate);
        studentDao = new StudentDaoImpl(jdbcTemplate);
    }

    @Test
    void addStudentToCourse_GivenStudent4AndCourse1_WhenAdded_ThenRowsAffectedIs1() {
        // Given
        int studentId = 4;
        int courseId = 1;

        // When
        int rowsAffected = studentCourses.addStudentToCourse(studentId, courseId);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void addStudentToCourse_GivenStudent4AndCourse1_WhenAdded_ThenStudentShouldBeSeenInCourse() {
        // Given
        int studentId = 4;
        int courseId = 1;
        studentCourses.addStudentToCourse(studentId, courseId);

        // When
        Student actual = studentDao.findAllStudentsByCourseName("Math").stream()
                .filter(student -> student.id() == studentId)
                .findFirst().orElseThrow(() -> new NoSuchElementException("No student found"));

        // Then
        assertEquals(studentDao.getById(studentId).get(), actual);
    }

    @Test
    void removeStudentFromCourse_GivenStudent2AndCourse1_WhenRemoved_ThenRowsAffectedIs1() {
        // Given
        int studentId = 2;
        int courseId = 1;

        // When
        int rowsAffected = studentCourses.removeStudentFromCourse(studentId, courseId);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void removeStudentFromCourse_GivenStudent2AndCourse1_WhenRemoved_ThenStudentNotInCourseList() {
        // Given
        int studentId = 2;
        int courseId = 1;
        studentCourses.addStudentToCourse(studentId, courseId);

        // When
        Optional<Student> actual = studentDao.findAllStudentsByCourseName("Math").stream()
                .filter(student -> student.id() == studentId)
                .findFirst();

        // Then
        assertEquals(studentDao.getById(studentId), Optional.empty());
    }
}
