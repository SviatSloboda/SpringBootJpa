package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = GenerateService.class)
class GenerateServiceTest {
    @Autowired
    GenerateService generateService;

    @MockBean
    GroupDao groupDao;

    @MockBean
    StudentDao studentDao;

    @MockBean
    CourseDao courseDao;

    @MockBean
    StudentCourses studentCourses;

    @Test
    public void generateGroupsShouldCreateTenGroups() {
        // Arrange
        when(groupDao.saveWithoutId(any(Group.class))).thenReturn(1);

        // Act
        generateService.generateGroups();

        // Assert
        verify(groupDao, times(10)).saveWithoutId(any(Group.class));
    }

    @Test
    public void generateCoursesShouldCreateTenCourses() {
        // Arrange
        when(courseDao.saveWithoutId(any(Course.class))).thenReturn(1);

        // Act
        generateService.generateCourses();

        // Assert
        verify(courseDao, times(10)).saveWithoutId(any(Course.class));
    }

    @Test
    public void generateStudentsShouldCreateTwoHundredStudents() {
        // Arrange
        when(studentDao.saveWithoutId(any(Student.class))).thenReturn(1);

        // Act
        generateService.generateStudents();

        // Assert
        verify(studentDao, times(200)).saveWithoutId(any(Student.class));
    }

    @Test
    public void assignStudentsToCoursesShouldAssignCoursesToStudents() {
        // Arrange
        when(studentCourses.addStudentToCourse(anyInt(), anyInt())).thenReturn(1);

        // Act
        generateService.assignStudentsToCourses();

        // Assert
        verify(studentCourses, atLeast(200)).addStudentToCourse(anyInt(), anyInt());
    }
}