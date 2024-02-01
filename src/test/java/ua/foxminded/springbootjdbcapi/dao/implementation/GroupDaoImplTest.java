package ua.foxminded.springbootjdbcapi.dao.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/drop_tables.sql"})
@Sql(scripts = {"/sql/create_tables.sql"})
@Sql(scripts = {"/sql/insert_data.sql"})
@ActiveProfiles("test")

@Testcontainers
class GroupDaoImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GroupDaoImpl groupDao;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl(jdbcTemplate);
    }

    @Test
    void deleteById_GivenId2_WhenDeleted_ThenRowsAffectedIs1() {
        // Given
        // When
        int rowsAffected = groupDao.deleteById(2);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void deleteById_GivenId1_WhenDeleted_ThenGroupIsRemoved() {
        // Given
        // When
        groupDao.deleteById(1);

        // Then
        assertEquals(Optional.empty(), groupDao.getById(1));
    }

    @Test
    void getAll_WhenCalled_ThenAllGroupNamesAreEqual() {
        // Given
        List<String> expected = List.of("Group A", "Group B");

        // When
        List<String> actual = groupDao.getAll().stream()
                .map(Group::groupName)
                .toList();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void getById_GivenNewlyCreatedGroup_WhenFetched_ThenReturnNewGroup() {
        // Given
        Group group = new Group(33, "test");
        groupDao.save(group);

        // When
        Group actual = groupDao.getById(33).get();

        // Then
        assertEquals(group, actual);
    }

    @Test
    void getById_GivenNonExistingId_WhenFetched_ThenReturnOptionalEmpty() {
        // Given
        // When
        Optional<Group> result = groupDao.getById(323);

        // Then
        assertEquals(Optional.empty(), result);
    }

    @Test
    void update_GivenGroupWithNewName_WhenUpdated_ThenChangeTheName() {
        // Given
        Group group = new Group(1, "test");

        // When
        groupDao.update(group);

        // Then
        assertEquals(group, groupDao.getById(1).get());
    }

    @Test
    void update_GivenGroupWithNewName_WhenUpdated_ThenRowsAffectedIs1() {
        // Given
        Group group = new Group(1, "test");

        // When
        int rowsAffected = groupDao.update(group);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void save_GivenNewGroup_WhenSaved_ThenRetrieveThisGroup() {
        // Given
        Group group = new Group(332, "test");

        // When
        groupDao.save(group);

        // Then
        assertEquals(group, groupDao.getById(332).get());
    }

    @Test
    void save_GivenNewGroup_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Group group = new Group(332, "test");

        // When
        int rowsAffected = groupDao.save(group);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void saveWithoutId_GivenGroupWithoutId_WhenSaved_ThenCanBeRetrieved() {
        // Given
        Group createGroup = new Group("test");

        // When
        groupDao.saveWithoutId(createGroup);
        Group actualGroup = groupDao.getAll().stream()
                .max(Comparator.comparing(Group::id))
                .orElseThrow(() -> new NoSuchElementException("No group found"));

        // Then
        Group expectedGroup = new Group(actualGroup.id(), createGroup.groupName());
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void saveWithoutId_GivenNewCourse_WhenSaved_ThenRowsAffectedIs1() {
        // Given
        Group newGroup = new Group("test");

        // When
        int rowsAffected = groupDao.save(newGroup);

        // Then
        assertEquals(1, rowsAffected);
    }

    @Test
    void findAllGroupsWithLessOrEqualStudentsNumber_GivenMaxStudents2_WhenSearched_ThenReturnMatchingGroups() {
        // Given
        List<Group> expected = List.of(new Group(2, "Group B"));

        // When
        List<Group> actual = groupDao.findAllGroupsWithLessOrEqualStudentsNumber(2);

        // Then
        assertEquals(expected, actual);
    }
}
