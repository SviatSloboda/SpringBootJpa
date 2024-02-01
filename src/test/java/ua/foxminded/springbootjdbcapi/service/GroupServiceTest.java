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

    @Test
    void shouldCreateNewGroupWithoutId() {
        // Arrange
        Group group = new Group("Math Description");
        when(groupDao.saveWithoutId(any(Group.class))).thenReturn(1);

        // Act
        boolean result = groupService.createGroupWithoutId(group);

        // Assert
        assertTrue(result);
        verify(groupDao).saveWithoutId(any(Group.class));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNewGroupWithoutIdWasNotCreated() {
        // Arrange
        Group group = new Group("test");
        when(groupDao.saveWithoutId(group)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> groupService.createGroupWithoutId(group));
    }

    @Test
    void shouldCreateGroupSuccessfully() {
        // Arrange
        Group group = new Group(1, "test");
        when(groupDao.save(group)).thenReturn(1);

        // Act
        boolean result = groupService.createGroup(group);

        // Assert
        assertTrue(result);
        verify(groupDao).save(any(Group.class));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNewGroupWasNotCreated() {
        // Arrange
        Group group = new Group(1, "test");
        when(groupDao.save(group)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> groupService.createGroup(group));
    }

    @Test
    void shouldDeleteGroupByIdSuccessfully() {
        // Arrange
        int id = 1;
        when(groupDao.existsById(id)).thenReturn(true);
        when(groupDao.deleteById(id)).thenReturn(1);

        // Act
        boolean result = groupService.deleteGroupById(id);

        // Assert
        assertTrue(result);
        verify(groupDao).existsById(id);
        verify(groupDao).deleteById(id);
    }

    @Test
    void shouldDeleteAllGroups() {
        // Arrange
        when(groupDao.deleteAll()).thenReturn(10);

        // Act
        boolean result = groupService.deleteAll();

        // Assert
        assertTrue(result);
        verify(groupDao).deleteAll();
    }

    @Test
    void shouldThrowIllegalStateException_whenAllGroupsWereNotDeleted() {
        // Arrange
        when(groupDao.deleteAll()).thenReturn(0);

        // Act
        assertThrows(IllegalStateException.class,
                () -> groupService.deleteAll(),
                "Courses were not deleted");
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenGroupNotExistsByDeleting() {
        // Arrange
        int id = 1;
        when(groupDao.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.deleteGroupById(id));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenGroupWasNotDeleted() {
        // Arrange
        int id = 1;
        when(groupDao.existsById(id)).thenReturn(true);
        when(groupDao.deleteById(id)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> groupService.deleteGroupById(id));
    }

    @Test
    void shouldUpdateGroupSuccessfully() {
        // Arrange
        Group group = new Group(1, "test");
        when(groupDao.existsById(group.id())).thenReturn(true);
        when(groupDao.update(group)).thenReturn(1);

        // Act
        boolean result = groupService.updateGroup(group);

        // Assert
        assertTrue(result);
        verify(groupDao).update(group);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenGroupNotExistsByUpdating() {
        // Arrange
        Group group = new Group(1, "test");
        when(groupDao.existsById(group.id())).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.updateGroup(group));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenGroupWasNotUpdated() {
        // Arrange
        Group group = new Group(1, "test");
        when(groupDao.existsById(group.id())).thenReturn(true);
        when(groupDao.update(group)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> groupService.updateGroup(group));
    }

    @Test
    void shouldBeTrueWhenGroupExistsById() {
        // Arrange
        int id = 1;
        when(groupDao.existsById(id)).thenReturn(true);

        // Act
        boolean result = groupService.groupExistsById(id);

        // Assert
        assertTrue(result);
        verify(groupDao).existsById(id);
    }

    @Test
    void shouldBeFalseWhenGroupDoesNotExistById() {
        // Arrange
        int id = 1;
        when(groupDao.existsById(id)).thenReturn(false);

        // Act
        boolean result = groupService.groupExistsById(id);

        // Assert
        assertFalse(result);
        verify(groupDao).existsById(id);
    }

    @Test
    void shouldGetAllGroupsSuccessfully() {
        // Arrange
        List<Group> expected = new ArrayList<>(List.of(new Group(1, "test")));
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
        int id = 1;
        Optional<Group> expected = Optional.of(new Group("test"));
        when(groupDao.getById(id)).thenReturn(expected);

        // Act
        Group actual = groupService.getGroupById(id);

        // Assert
        assertEquals(expected.get(), actual);
        verify(groupDao).getById(id);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenGroupByIdNotFound() {
        // Arrange
        int id = 1;
        Optional<Group> expected = Optional.empty();
        when(groupDao.getById(id)).thenReturn(expected);

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> groupService.getGroupById(id));
    }

    @Test
    void shouldFindAllGroupsWithLessOrEqualsStudentCountSuccessfully() {
        // Arrange
        int studentCount = 1;
        List<Group> expected = new ArrayList<>(List.of(new Group(1, "test")));
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
