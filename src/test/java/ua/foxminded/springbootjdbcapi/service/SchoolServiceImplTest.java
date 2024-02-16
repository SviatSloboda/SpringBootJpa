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
import ua.foxminded.springbootjdbcapi.service.SchoolService;
import ua.foxminded.springbootjdbcapi.service.StudentService;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        SchoolService.class, StudentService.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/drop_tables.sql", "/sql/create_tables.sql", "/sql/insert_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Testcontainers
class SchoolServiceImplTest {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    StudentService studentDao;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");


    @Test
    void enrollStudentToCourse_GivenStudent4AndCourse1_WhenAdded_ThenRowsAffectedIs1() {
        // Given
        String studentId = "4";
        String courseId = "1";

        // When
        boolean wasAdded = schoolService.addStudentToCourse(studentId, courseId);

        // Then
        assertTrue(wasAdded);
    }

    @Test
    void enrollStudentToCourse_GivenStudent4AndCourse1_WhenAdded_ThenStudentShouldBeSeenInCourse() {
        // Given
        String studentId = "4";
        String courseId = "1";
        schoolService.addStudentToCourse(studentId, courseId);

        // When
        Student actual = studentDao.findAllStudentsByCourseName("Math").stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No student found"));

        // Then
        assertEquals(studentDao.getById(studentId), actual);
    }

    @Test
    void withdrawStudentFromCourse_GivenStudent2AndCourse1_WhenRemoved_ThenRowsAffectedIs1() {
        // Given
        String studentId = "2";
        String courseId = "1";

        // When
        boolean wasRemoved = schoolService.removeStudentFromCourse(studentId, courseId);

        // Then
        assertTrue(wasRemoved);
    }

    @Test
    void withdrawStudentFromCourse_GivenStudent2AndCourse1_WhenRemoved_ThenStudentNotInCourseList() {
        // Given
        String studentId = "2";
        String courseId = "1";

        // When
        boolean wasRemoved = schoolService.removeStudentFromCourse(studentId, courseId);

        // Then
        List<Student> studentsInMathCourse = studentDao.findAllStudentsByCourseName("Math");

        boolean studentExistsInCourse = studentsInMathCourse.stream()
                .anyMatch(student -> student.getId().equals(studentId));

        assertTrue(wasRemoved);
        assertFalse(studentExistsInCourse, "Student should not be in the Math course list after removal.");
    }

}

