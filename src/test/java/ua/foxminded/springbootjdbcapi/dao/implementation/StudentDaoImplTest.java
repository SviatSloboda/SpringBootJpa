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
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        StudentDao.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/drop_tables.sql", "/sql/create_tables.sql", "/sql/insert_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Testcontainers
class StudentDaoImplTest {
    @Autowired
    private StudentDao studentDao;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void removeById_GivenId1_WhenDeleted_ThenRowsAffectedAndStudentRemoved() {
        // Given
        // When
        boolean wasDeleted = studentDao.deleteById("1");

        // Then
        assertTrue(wasDeleted);
        assertEquals(studentDao.getById("1"), Optional.empty());
    }

    @Test
    void removeById_GivenNonExistingId_WhenDeleted_ThenNoRowsAffected() {
        // Given
        // When
        boolean wasDeleted = studentDao.deleteById("999");

        // Then
        assertFalse(wasDeleted);
    }

    @Test
    void modify_GivenStudentWithId1_WhenUpdated_ThenUpdateFirstNameAndLastName() {
        // Given
        Student testStudent = new Student("1", new Group("1", "test"), "test", "test");

        // When
        boolean wasUpdated = studentDao.update(testStudent);

        // Then
        assertTrue(wasUpdated);
        assertEquals("test", studentDao.getById("1").get().getFirstName());
        assertEquals("test", studentDao.getById("1").get().getLastName());
    }

    @Test
    void modify_GivenNonExistingStudent_WhenUpdated_ThenRowsAffectedIs0() {
        // Given
        Student testStudent = new Student("999", new Group("1", "test"), "test", "test");

        // When
        boolean wasUpdated = studentDao.update(testStudent);

        // Then
        assertFalse(wasUpdated);
    }

    @Test
    void listAll_WhenCalled_ThenRetrieveCorrectListOfStudents() {
        // Given
        List<Student> expectedStudents = List.of(
                new Student("1", new Group("1", "Group A"), "John", "Doe"),
                new Student("2", new Group("1", "Group A"), "Alice", "Smith"),
                new Student("3", new Group("2", "Group B"), "Bob", "Johnson"),
                new Student("4", new Group("1", "Group A"), "Eva", "Brown")
        );

        // When
        List<Student> actualStudents = studentDao.getAll();

        // Then
        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void retrieveById_GivenStudentId1_WhenFetched_ThenRetrieveStudentWithId1() {
        // Given
        Group expectedGroup = new Group("1", "Group A");
        Student expectedStudent = new Student("1", expectedGroup, "John", "Doe");

        // When
        Optional<Student> actualStudentOpt = studentDao.getById("1");
        assertTrue(actualStudentOpt.isPresent());
        Student actualStudent = actualStudentOpt.get();

        // Then
        assertEquals(expectedStudent.getId(), actualStudent.getId());
        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
        assertEquals(expectedStudent.getLastName(), actualStudent.getLastName());
        assertNotNull(actualStudent.getGroup());
        assertEquals(expectedGroup.getId(), actualStudent.getGroup().getId());
        assertEquals(expectedGroup.getGroupName(), actualStudent.getGroup().getGroupName());
    }


    @Test
    void save_GivenNewStudent_WhenSaved_ThenRetrieveThisStudent() {
        // Given
        Student exampleStudent = new Student("444", new Group("2", "test"), "test", "test");
        studentDao.save(exampleStudent);

        // When
        Student actual = studentDao.getById("444").get();

        // Then
        assertEquals(exampleStudent, actual);
    }

    @Test
    void findAllStudentsByCourseName_GivenCourseMath_WhenSearched_ThenRetrieve2Students() {
        // Given
        Group expectedGroup = new Group("1", "Group A");
        List<Student> expected = List.of(
                new Student("1", expectedGroup, "John", "Doe"),
                new Student("2", expectedGroup, "Alice", "Smith")
        );

        expected.forEach(student -> student.getGroup().setGroupName("Group A"));

        // When
        List<Student> actual = studentDao.findAllStudentsByCourseName("Math");

        // Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.stream().anyMatch(student -> "John".equals(student.getFirstName()) && "Doe".equals(student.getLastName())));
        assertTrue(actual.stream().anyMatch(student -> "Alice".equals(student.getFirstName()) && "Smith".equals(student.getLastName())));
    }


    @Test
    void save_GivenNewStudent_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Student newStudent = new Student("111", new Group("1", "test"), "test", "test");

        // When
        boolean wasSaved = studentDao.save(newStudent);

        // Then
        assertTrue(wasSaved);
    }
}
