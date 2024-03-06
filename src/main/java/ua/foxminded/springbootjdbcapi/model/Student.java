package ua.foxminded.springbootjdbcapi.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

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
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "student_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(mappedBy = "students")
    private List<Course> courses;
    public Student(String id, Group group, String firstName, String lastName) {
        this.id = id;
        this.group = group;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(Group group, String firstName, String lastName) {
        this(UUID.randomUUID().toString(), group, firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(group, student.group) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Student{" +
               "id='" + id + '\'' +
               ", group=" + group +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               '}';
    }
}
