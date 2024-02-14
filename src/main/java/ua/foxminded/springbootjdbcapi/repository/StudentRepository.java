package ua.foxminded.springbootjdbcapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.springbootjdbcapi.model.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
}
