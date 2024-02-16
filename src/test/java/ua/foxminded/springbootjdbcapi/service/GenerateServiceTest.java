package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GenerateServiceTest {

    @InjectMocks
    private GenerateService generateService;

    @Mock
    private StudentService studentService;

    @Mock
    private SchoolService schoolService;

    @Mock
    private CourseService courseService;

    @Mock
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        //arrange
        MockitoAnnotations.openMocks(this);
        when(studentService.save(any(Student.class))).thenReturn(true);
        when(groupService.save(any(Group.class))).thenReturn(true);
        when(courseService.save(any(Course.class))).thenReturn(true);
        when(schoolService.addStudentToCourse(anyString(), anyString())).thenReturn(true);

        when(groupService.getAllIds()).thenReturn(Arrays.asList("1", "2"));
        when(groupService.getById(anyString())).thenReturn(new Group("Test Group"));
        when(studentService.getAllIds()).thenReturn(Arrays.asList("student1", "student2"));
        when(courseService.getAllIds()).thenReturn(Arrays.asList("course1", "course2"));
    }

    @Test
    void deleteAllShouldInvokeDeleteMethods() {
        //act
        generateService.deleteAll();

        //assert
        verify(schoolService, times(1)).deleteAll();
        verify(groupService, times(1)).deleteAll();
    }

    @Test
    void generateGroupsShouldInvokeSaveMethod() {
        //act
        generateService.generateGroups();

        //assert
        verify(groupService, times(10)).save(any(Group.class));
    }

    @Test
    void generateCoursesShouldInvokeSaveMethod() {
        //act
        generateService.generateCourses();

        //assert
        verify(courseService, times(10)).save(any(Course.class));
    }

    @Test
    void generateStudentsShouldInvokeSaveMethod() {
        //act
        generateService.generateStudents();

        //assert
        verify(studentService, times(200)).save(any(Student.class));
    }
}
