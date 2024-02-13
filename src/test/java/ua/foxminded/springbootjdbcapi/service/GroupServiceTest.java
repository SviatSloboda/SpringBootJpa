package ua.foxminded.springbootjdbcapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GroupService.class})
class GroupServiceTest {
    @MockBean
    GroupDao groupDao;

    @Autowired
    GroupService groupService;

    private final Group group = new Group("1", "test");

    @Test
    void shouldCreateGroupSuccessfully() {
        // Arrange
        Group group = new Group("1", "test");
        when(groupDao.save(group)).thenReturn(true);

        // Act
        boolean result = groupService.save(group);

        // Assert
        assertTrue(result);
        verify(groupDao).save(any(Group.class));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNewGroupWasNotCreated() {
        // Arrange

        when(groupDao.save(group)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> groupService.save(group));
    }

    @Test
    void shouldDeleteGroupByIdSuccessfully() {
        // Arrange
        String id = "1";
        when(groupDao.existsById(id)).thenReturn(true);
        when(groupDao.deleteById(id)).thenReturn(true);

        // Act
        boolean result = groupService.deleteById(id);

        // Assert
        assertTrue(result);
        verify(groupDao).existsById(id);
        verify(groupDao).deleteById(id);
    }

    @Test
    void shouldDeleteAllGroups() {
        // Arrange
        when(groupDao.deleteAll()).thenReturn(true);

        // Act
        boolean result = groupService.deleteAll();

        // Assert
        assertTrue(result);
        verify(groupDao).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllGroupsWereNotDeleted() {
        // Arrange
        when(groupDao.deleteAll()).thenReturn(false);

        // Act
        assertThrows(IllegalStateException.class,
                () -> groupService.deleteAll(),
                "Courses were not deleted");
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenGroupNotExistsByDeleting() {
        // Arrange
        String id = "1";
        when(groupDao.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.deleteById(id));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenGroupWasNotDeleted() {
        // Arrange
        String id = "1";
        when(groupDao.existsById(id)).thenReturn(true);
        when(groupDao.deleteById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> groupService.deleteById(id));
    }

    @Test
    void shouldUpdateGroupSuccessfully() {
        // Arrange
        when(groupDao.existsById(group.getId())).thenReturn(true);
        when(groupDao.update(group)).thenReturn(true);

        // Act
        boolean result = groupService.update(group);

        // Assert
        assertTrue(result);
        verify(groupDao).update(group);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenGroupNotExistsByUpdating() {
        // Arrange
        when(groupDao.existsById(group.getId())).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.update(group));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenGroupWasNotUpdated() {
        // Arrange
        when(groupDao.existsById(group.getId())).thenReturn(true);
        when(groupDao.update(group)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> groupService.update(group));
    }

    @Test
    void shouldBeTrueWhenGroupExistsById() {
        // Arrange
        String id = "1";
        when(groupDao.existsById(id)).thenReturn(true);

        // Act
        boolean result = groupService.existsById(id);

        // Assert
        assertTrue(result);
        verify(groupDao).existsById(id);
    }

    @Test
    void shouldBeFalseWhenGroupDoesNotExistById() {
        // Arrange
        String id = "1";
        when(groupDao.existsById(id)).thenReturn(false);

        // Act
        boolean result = groupService.existsById(id);

        // Assert
        assertFalse(result);
        verify(groupDao).existsById(id);
    }

    @Test
    void shouldGetAllGroupsSuccessfully() {
        // Arrange
        List<Group> expected = new ArrayList<>(List.of(new Group("1", "test")));
        when(groupDao.getAll()).thenReturn(expected);

        // Act
        List<Group> actual = groupService.getAllGroups();

        // Assert
        assertEquals(expected, actual);
        verify(groupDao).getAll();
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNoGroupsFound() {
        // Arrange
        List<Group> expected = new ArrayList<>();
        when(groupDao.getAll()).thenReturn(expected);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.getAllGroups());
    }

    @Test
    void shouldGetGroupByIdSuccessfully() {
        // Arrange
        String id = "1";
        Optional<Group> expected = Optional.of(new Group("test"));
        when(groupDao.getById(id)).thenReturn(expected);

        // Act
        Group actual = groupService.getById(id);

        // Assert
        assertEquals(expected.get(), actual);
        verify(groupDao).getById(id);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenGroupByIdNotFound() {
        // Arrange
        String id = "1";
        Optional<Group> expected = Optional.empty();
        when(groupDao.getById(id)).thenReturn(expected);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.getById(id));
    }


    @Test
    void shouldFindAllGroupsWithLessOrEqualsStudentCountSuccessfully() {
        // Arrange
        int studentCount = 1;
        List<Group> expected = new ArrayList<>(List.of(new Group("1", "test")));
        when(groupDao.findAllGroupsWithLessOrEqualStudentsNumber(studentCount)).thenReturn(expected);

        // Act
        List<Group> actual = groupService.findAllGroupsWithLessOrEqualsStudentCount(studentCount);

        // Assert
        assertEquals(expected, actual);
        verify(groupDao).findAllGroupsWithLessOrEqualStudentsNumber(studentCount);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStudentCountIsNegativeForFindingGroupsWithLessOrEqualsStudentCount() {
        // Arrange
        int studentCount = -1;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> groupService.findAllGroupsWithLessOrEqualsStudentCount(studentCount),
                "Student count cannot be less than 0");
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNoGroupsFoundByStudentCountForFindingGroupsWithLessOrEqualsStudentCount() {
        // Arrange
        int studentCount = 1;
        List<Group> expected = new ArrayList<>();
        when(groupDao.findAllGroupsWithLessOrEqualStudentsNumber(studentCount)).thenReturn(expected);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.findAllGroupsWithLessOrEqualsStudentCount(studentCount),
                "No groups with student count less or equal to " + studentCount + " were found");
    }
}
