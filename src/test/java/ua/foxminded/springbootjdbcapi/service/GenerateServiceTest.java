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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
        when(groupDao.save(any(Group.class))).thenReturn(true);

        // Act
        generateService.generateGroups();

        // Assert
        verify(groupDao, times(10)).save(any(Group.class));
    }

    @Test
    public void generateCoursesShouldCreateTenCourses() {
        // Arrange
        when(courseDao.save(any(Course.class))).thenReturn(true);

        // Act
        generateService.generateCourses();

        // Assert
        verify(courseDao, times(10)).save(any(Course.class));
    }

    @Test
    public void generateStudentsShouldCreateTwoHundredStudents() {
        // Arrange
        List<String> mockGroupIds = Arrays.asList("group1", "group2");
        when(groupDao.getAllIds()).thenReturn(mockGroupIds);
        when(groupDao.getById(anyString())).thenReturn(Optional.of(new Group("Test Group")));
        when(studentDao.save(any(Student.class))).thenReturn(true);

        // Act
        generateService.generateStudents();

        // Assert
        verify(studentDao, times(200)).save(any(Student.class));
    }

    @Test
    public void assignStudentsToCoursesShouldAssignCorrectly() {
        // Arrange
        List<String> mockStudentIds = Arrays.asList("student1", "student2", "student3");
        List<String> mockCourseIds = Arrays.asList("course1", "course2", "course3");
        when(studentDao.getAllIds()).thenReturn(mockStudentIds);
        when(courseDao.getAllIds()).thenReturn(mockCourseIds);
        when(studentCourses.addStudentToCourse(anyString(), anyString())).thenReturn(true);

        // Act
        generateService.assignStudentsToCourses();

        // Assert
        verify(studentCourses, atLeast(mockStudentIds.size())).addStudentToCourse(anyString(), anyString());
    }
}
