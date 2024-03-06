package ua.foxminded.springbootjdbcapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "courses")
public class Course {
    @Id
    @Column(name = "course_id")
    private String id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_description")
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    public Course(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description){
        this(UUID.randomUUID().toString(), name, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(description, course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "Course{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
