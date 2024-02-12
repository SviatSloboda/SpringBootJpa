package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Student;

@Repository
@RequiredArgsConstructor
public class StudentCoursesImpl implements StudentCourses {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public boolean addStudentToCourse(String studentId, String courseId) {
        Student student = em.find(Student.class, studentId);
        Course course = em.find(Course.class, courseId);

        try {
            course.getStudents().add(student);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    public boolean removeStudentFromCourse(String studentId, String courseId) {
        Student student = em.find(Student.class, studentId);
        Course course = em.find(Course.class, courseId);

        try {
            course.getStudents().remove(student);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    public boolean studentEnrolledOnCourse(String studentId, String courseId) {
        Student student = em.find(Student.class, studentId);
        Course course = em.find(Course.class, courseId);

        return course.getStudents().contains(student);
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        try {
            em.createQuery("SELECT c FROM Course c JOIN c.students s", Course.class)
                    .getResultList()
                    .forEach(course -> course.getStudents().clear());

            em.createQuery("SELECT s FROM Student s JOIN s.courses c", Student.class)
                    .getResultList()
                    .forEach(student -> student.getCourses().clear());

            em.createQuery("UPDATE Student s SET s.group = null").executeUpdate();
            em.createQuery("DELETE FROM Group").executeUpdate();
            em.createQuery("DELETE FROM Student").executeUpdate();
            em.createQuery("DELETE FROM Course").executeUpdate();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}