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
import ua.foxminded.springbootjdbcapi.service.GroupService;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        GroupService.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/drop_tables.sql", "/sql/create_tables.sql", "/sql/insert_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GroupServiceImplTest {

    @Autowired
    private GroupService groupService;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void removeById_GivenId2_WhenDeleted_ThenRowsAffectedIs1() {
        // Given
        // When
        boolean wasDeleted = groupService.deleteById("2");

        // Then
        assertTrue(wasDeleted);
    }


    @Test
    void removeById_GivenId1_WhenDeleted_ThenGroupIsRemoved() {
        // Given
        // When
        groupService.deleteById("1");

        // Then
        assertThrows(NoSuchElementException.class, () ->  groupService.getById("1"), "There is no such group with ID: 1");
    }

    @Test
    void listAll_WhenCalled_ThenAllGroupNamesAreEqual() {
        // Given
        List<String> expected = List.of("Group A", "Group B");

        // When
        List<String> actual = groupService.getAllGroups().stream()
                .map(Group::getGroupName)
                .toList();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void retrieveById_GivenNewlyCreatedGroup_WhenFetched_ThenReturnNewGroup() {
        // Given
        Group group = new Group("33", "test");
        groupService.save(group);

        // When
        Group actual = groupService.getById("33");

        // Then
        assertEquals(group, actual);
    }

    @Test
    void retrieveById_GivenNonExistingId_WhenFetched_ThenReturnOptionalEmpty() {
        // When & Then
        assertThrows(NoSuchElementException.class, () -> groupService.getById("323"), "There is no such group with ID: 323");
    }

    @Test
    void modify_GivenGroupWithNewName_WhenUpdated_ThenChangeTheName() {
        // Given
        Group group = new Group("1", "test");

        // When
        groupService.update(group);

        // Then
        assertEquals(group, groupService.getById("1"));
    }

    @Test
    void modify_GivenGroupWithNewName_WhenUpdated_ThenRowsAffectedIs1() {
        // Given
        Group group = new Group("1", "test");

        // When
        boolean wasUpdated = groupService.update(group);

        // Then
        assertTrue(wasUpdated);
    }

    @Test
    void save_GivenNewGroup_WhenSaved_ThenRetrieveThisGroup() {
        // Given
        Group group = new Group("332", "test");

        // When
        groupService.save(group);

        // Then
        assertEquals(group, groupService.getById("332"));
    }

    @Test
    void save_GivenNewGroup_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Group group = new Group("332", "test");

        // When
        boolean wasSaved = groupService.save(group);

        // Then
        assertTrue(wasSaved);
    }

    @Test
    void findAllGroupsWithLessOrEqualStudentsNumber_GivenMaxStudents2_WhenSearched_ThenReturnMatchingGroups() {
        // Given
        List<Group> expected = List.of(new Group("2", "Group B"));

        // When
        List<Group> actual = groupService.findAllGroupsWithLessOrEqualsStudentCount(2);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void existsById_GivenExistingId_WhenChecked_ThenReturnsTrue() {
        // Given
        String existingId = "1";

        // When
        boolean exists = groupService.existsById(existingId);

        // Then
        assertTrue(exists);
    }

    @Test
    void existsById_GivenNonExistingId_WhenChecked_ThenReturnsFalse() {
        // Given
        String nonExistingId = "999";

        // When
        boolean exists = groupService.existsById(nonExistingId);

        // Then
        assertFalse(exists);
    }

    @Test
    void deleteAll_WhenCalled_ThenAllGroupsAreRemoved() {
        // Given

        // When
        groupService.deleteAll();

        // Then
        assertThrows(NoSuchElementException.class, () -> groupService.getAllGroups() ,"No groups were found");
    }

    @Test
    void getAllIds_WhenCalled_ThenReturnsListOfAllIds() {
        // Given

        // When
        List<String> actualIds = groupService.getAllIds();

        // Then
        assertNotNull(actualIds);
        assertFalse(actualIds.isEmpty());
    }

    @Test
    void findAllGroupsWithLessOrEqualsStudentCount_GivenNoGroupsMatch_WhenSearched_ThenReturnEmptyList() {
        // Given
        int studentCount = 0;

        // When
        // Then
        assertThrows(NoSuchElementException.class,() -> groupService.findAllGroupsWithLessOrEqualsStudentCount(studentCount),"There are no such groups with less or equals student count: " + studentCount);
    }

}
