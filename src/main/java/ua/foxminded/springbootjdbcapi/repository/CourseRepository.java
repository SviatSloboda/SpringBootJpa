package ua.foxminded.springbootjdbcapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.springbootjdbcapi.model.Course;

public interface CourseRepository extends JpaRepository<Course, String> {
}
