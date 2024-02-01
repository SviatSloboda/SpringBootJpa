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
class StudentDaoImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
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
        studentDao = new StudentDaoImpl(jdbcTemplate);
    }

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenRowsAffectedAndStudentRemoved() {
        // Given
        // When
        int rowsAffected = studentDao.deleteById(1);

        // Then
        assertEquals(2, rowsAffected);
        assertEquals(studentDao.getById(1), Optional.empty());
    }

    @Test
    void deleteById_GivenNonExistingId_WhenDeleted_ThenNoRowsAffected() {
        // Given
        // When
        int rowsAffected = studentDao.deleteById(999);

        // Then
        assertEquals(0, rowsAffected);
    }

    @Test
    void update_GivenStudentWithId1_WhenUpdated_ThenUpdateFirstNameAndLastName() {
        // Given
        Student testStudent = new Student(1, 1, "test", "test");

        // When
        int actual = studentDao.update(testStudent);

        // Then
        assertEquals(1, actual);
        assertEquals("test", studentDao.getById(1).get().firstName());
        assertEquals("test", studentDao.getById(1).get().lastName());
    }

    @Test
    void update_GivenNonExistingStudent_WhenUpdated_ThenRowsAffectedIs0() {
        // Given
        Student testStudent = new Student(999, 1, "test", "test");

        // When
        int actual = studentDao.update(testStudent);

        // Then
        assertEquals(0, actual);
    }

    @Test
    void getAll_WhenCalled_ThenRetrieveCorrectListOfStudents() {
        // Given
        List<Student> expectedStudents = List.of(
                new Student(1, 1, "John", "Doe"),
                new Student(2, 1, "Alice", "Smith"),
                new Student(3, 2, "Bob", "Johnson"),
                new Student(4, 1, "Eva", "Brown")
        );

        // When
        List<Student> actualStudents = studentDao.getAll();

        // Then
        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void getById_GivenStudentId1_WhenFetched_ThenRetrieveStudentWithId1() {
        // Given
        Student exampleStudent = new Student(1, 1, "John", "Doe");

        // When
        Student actual = studentDao.getById(1).get();

        // Then
        assertEquals(exampleStudent, actual);
    }

    @Test
    void save_GivenNewStudent_WhenSaved_ThenRetrieveThisStudent() {
        // Given
        Student exampleStudent = new Student(444, 2, "test", "test");
        studentDao.save(exampleStudent);

        // When
        Student actual = studentDao.getById(444).get();

        // Then
        assertEquals(exampleStudent, actual);
    }

    @Test
    void findAllStudentsByCourseName_GivenCourseMath_WhenSearched_ThenRetrieve2Students() {
        // Given
        List<Student> expected = List.of(
                new Student(1, 1, "John", "Doe"),
                new Student(2, 1, "Alice", "Smith")
        );

        // When
        List<Student> actual = studentDao.findAllStudentsByCourseName("Math");

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void save_GivenNewStudent_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Student newStudent = new Student(111, 1, "test", "test");

        // When
        int rowsAffected = studentDao.save(newStudent);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void saveWithoutId_GivenStudentWithoutId_WhenSaved_ThenCanBeRetrieved() {
        // Given
        Student createStudent = new Student(1, "test", "test");

        // When
        studentDao.saveWithoutId(createStudent);
        Student actualStudent = studentDao.getAll().stream()
                .max(Comparator.comparing(Student::id))
                .orElseThrow(() -> new NoSuchElementException("No student found"));

        // Then
        Student expectedStudent = new Student(actualStudent.id(), createStudent.groupId(), createStudent.firstName(), createStudent.lastName());
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void saveWithoutId_GivenNewStudent_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Student newStudent = new Student(1, "test", "test");

        // When
        int rowsAffected = studentDao.save(newStudent);

        // Then
        assertEquals(1, rowsAffected);
    }
}
