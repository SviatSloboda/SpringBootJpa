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
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        GroupDao.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/drop_tables.sql", "/sql/create_tables.sql", "/sql/insert_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GroupDaoImplTest {

    @Autowired
    private GroupDao groupDao;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void removeById_GivenId2_WhenDeleted_ThenRowsAffectedIs1() {
        // Given
        // When
        boolean wasDeleted = groupDao.deleteById("2");

        // Then
        assertTrue(wasDeleted);
    }


    @Test
    void removeById_GivenId1_WhenDeleted_ThenGroupIsRemoved() {
        // Given
        // When
        groupDao.deleteById("1");

        // Then
        assertEquals(Optional.empty(), groupDao.getById("1"));
    }

    @Test
    void listAll_WhenCalled_ThenAllGroupNamesAreEqual() {
        // Given
        List<String> expected = List.of("Group A", "Group B");

        // When
        List<String> actual = groupDao.getAll().stream()
                .map(Group::getGroupName)
                .toList();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void retrieveById_GivenNewlyCreatedGroup_WhenFetched_ThenReturnNewGroup() {
        // Given
        Group group = new Group("33", "test");
        groupDao.save(group);

        // When
        Group actual = groupDao.getById("33").get();

        // Then
        assertEquals(group, actual);
    }

    @Test
    void retrieveById_GivenNonExistingId_WhenFetched_ThenReturnOptionalEmpty() {
        // Given
        // When
        Optional<Group> result = groupDao.getById("323");

        // Then
        assertEquals(Optional.empty(), result);
    }

    @Test
    void modify_GivenGroupWithNewName_WhenUpdated_ThenChangeTheName() {
        // Given
        Group group = new Group("1", "test");

        // When
        groupDao.update(group);

        // Then
        assertEquals(group, groupDao.getById("1").get());
    }

    @Test
    void modify_GivenGroupWithNewName_WhenUpdated_ThenRowsAffectedIs1() {
        // Given
        Group group = new Group("1", "test");

        // When
        boolean wasUpdated = groupDao.update(group);

        // Then
        assertTrue(wasUpdated);
    }

    @Test
    void save_GivenNewGroup_WhenSaved_ThenRetrieveThisGroup() {
        // Given
        Group group = new Group("332", "test");

        // When
        groupDao.save(group);

        // Then
        assertEquals(group, groupDao.getById("332").get());
    }

    @Test
    void save_GivenNewGroup_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Group group = new Group("332", "test");

        // When
        boolean wasSaved = groupDao.save(group);

        // Then
        assertTrue(wasSaved);
    }

    @Test
    void findAllGroupsWithLessOrEqualStudentsNumber_GivenMaxStudents2_WhenSearched_ThenReturnMatchingGroups() {
        // Given
        List<Group> expected = List.of(new Group("2", "Group B"));

        // When
        List<Group> actual = groupDao.findAllGroupsWithLessOrEqualStudentsNumber(2);

        // Then
        assertEquals(expected, actual);
    }
}
