package ua.foxminded.springbootjdbcapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.springbootjdbcapi.model.Group;

public interface GroupRepository extends JpaRepository<Group, String> {
}