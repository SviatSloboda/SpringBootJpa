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
import ua.foxminded.springbootjdbcapi.service.StudentService;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        StudentService.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/drop_tables.sql", "/sql/create_tables.sql", "/sql/insert_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Testcontainers
class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void removeById_GivenId1_WhenDeleted_ThenRowsAffectedAndStudentRemoved() {
        // Given
        // When
        studentService.deleteById("1");

        // Then
        assertThrows(NoSuchElementException.class, () -> studentService.getById("1"), "There is no such student with ID: 1");
    }

    @Test
    void removeById_GivenNonExistingId_WhenDeleted_ThenNoRowsAffected() {
        // Given
        // When
        boolean wasDeleted = studentService.deleteById("999");

        // Then
        assertFalse(wasDeleted);
    }

    @Test
    void modify_GivenStudentWithId1_WhenUpdated_ThenUpdateFirstNameAndLastName() {
        // Given
        Student testStudent = new Student("1", new Group("1", "test"), "test", "test");

        // When
        boolean wasUpdated = studentService.update(testStudent);

        // Then
        assertTrue(wasUpdated);
        assertEquals("test", studentService.getById("1").getFirstName());
        assertEquals("test", studentService.getById("1").getLastName());
    }

    @Test
    void modify_GivenNonExistingStudent_WhenUpdated_ThenRowsAffectedIs0() {
        // Given
        Student testStudent = new Student("999", new Group("1", "test"), "test", "test");

        // When
        // Then
        assertThrows(NoSuchElementException.class, () ->  studentService.update(testStudent), "Student with ID 999 does not exist.");
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
        List<Student> actualStudents = studentService.getAllStudents();

        // Then
        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void retrieveById_GivenStudentId1_WhenFetched_ThenRetrieveStudentWithId1() {
        // Given
        Group expectedGroup = new Group("1", "Group A");
        Student expectedStudent = new Student("1", expectedGroup, "John", "Doe");

        // When
        Student actualStudentOpt = studentService.getById("1");
        assertTrue(actualStudentOpt != null);
        Student actualStudent = actualStudentOpt;

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
        Student exampleStudent = new Student("444", new Group("2", "Group B"), "test", "test");
        studentService.save(exampleStudent);

        // When
        Student actual = studentService.getById("444");

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
        List<Student> actual = studentService.findAllStudentsByCourseName("Math");

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
        boolean wasSaved = studentService.save(newStudent);

        // Then
        assertTrue(wasSaved);
    }

    @Test
    void saveStudentWithOwnId_GivenStudentWithExistingId_WhenSaved_ThenThrowIllegalStateException() {
        // Given
        Student existingStudent = new Student("1", new Group("1", "test"), "test", "test");

        // When/Then
        assertThrows(IllegalStateException.class, () -> studentService.saveStudentWithOwnId(existingStudent));
    }

    @Test
    void saveStudentWithOwnId_GivenStudentWithUniqueId_WhenSaved_ThenReturnTrue() {
        // Given
        Student uniqueStudent = new Student("999", new Group("1", "test"), "test", "test");

        // When
        boolean wasSaved = studentService.saveStudentWithOwnId(uniqueStudent);

        // Then
        assertTrue(wasSaved);
    }

    @Test
    void deleteAll_ShouldDeleteAllStudents() {
        // When
        boolean allDeleted = studentService.deleteAll();

        // Then
        assertTrue(allDeleted);
    }

    @Test
    void update_GivenExistingStudent_WhenUpdated_ThenReturnTrue() {
        // Given
        Student existingStudent = new Student("1", new Group("1", "test"), "test", "updated");

        // When
        boolean wasUpdated = studentService.update(existingStudent);

        // Then
        assertTrue(wasUpdated);
    }

    @Test
    void update_GivenNonExistingStudent_WhenUpdated_ThenThrowNoSuchElementException() {
        // Given
        Student nonExistingStudent = new Student("999", new Group("1", "test"), "test", "test");

        // When/Then
        assertThrows(NoSuchElementException.class, () -> studentService.update(nonExistingStudent));
    }

    @Test
    void deleteById_GivenExistingId_WhenDeleted_ThenReturnTrue() {
        // When
        boolean wasDeleted = studentService.deleteById("1");

        // Then
        assertTrue(wasDeleted);
    }

    @Test
    void deleteById_GivenNonExistingId_WhenDeleted_ThenReturnFalse() {
        // When
        boolean wasDeleted = studentService.deleteById("999");

        // Then
        assertFalse(wasDeleted);
    }

    @Test
    void existsById_GivenExistingId_ThenReturnTrue() {
        // When
        boolean exists = studentService.existsById("1");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsById_GivenNonExistingId_ThenReturnFalse() {
        // When
        boolean exists = studentService.existsById("999");

        // Then
        assertFalse(exists);
    }

    @Test
    void getAllStudents_WhenCalled_ThenRetrieveListOfStudents() {
        // When
        List<Student> students = studentService.getAllStudents();

        // Then
        assertNotNull(students);
        assertFalse(students.isEmpty());
    }

    @Test
    void getById_GivenExistingId_WhenFetched_ThenRetrieveStudent() {
        // When
        Student student = studentService.getById("1");

        // Then
        assertNotNull(student);
    }

    @Test
    void getById_GivenNonExistingId_WhenFetched_ThenThrowNoSuchElementException() {
        // When/Then
        assertThrows(NoSuchElementException.class, () -> studentService.getById("999"));
    }

    @Test
    void findAllStudentsByCourseName_GivenExistingCourseName_WhenSearched_ThenRetrieveStudents() {
        // When
        List<Student> students = studentService.findAllStudentsByCourseName("Math");

        // Then
        assertNotNull(students);
        assertFalse(students.isEmpty());
    }

    @Test
    void findAllStudentsByCourseName_GivenNonExistingCourseName_WhenSearched_ThenThrowNoSuchElementException() {
        // When/Then
        assertThrows(NoSuchElementException.class, () -> studentService.findAllStudentsByCourseName("NonexistentCourse"));
    }

}
