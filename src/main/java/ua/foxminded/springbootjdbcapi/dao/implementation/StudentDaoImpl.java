package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentDaoImpl implements StudentDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Student student = em.find(Student.class, id);

        if(student == null) return false;

        em.remove(student);

        return em.find(Student.class, id) == null;
    }

    @Override
    @Transactional
    public boolean update(Student object) {
        if(em.find(Student.class, object.getId()) == null) return false;

        try {
            em.merge(object);
            return true;
        } catch (PersistenceException e){
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        try {
            em.createQuery("DELETE FROM Student").executeUpdate();
            return true;
        } catch (PersistenceException e){
            return false;
        }
    }

    @Override
    public List<Student> getAll() {
        TypedQuery<Student> query = em.createQuery("SELECT s from Student s", Student.class);
        return query.getResultList();
    }

    @Override
    public Optional<Student> getById(String id) {
        return Optional.ofNullable(em.find(Student.class, id));
    }

    @Override
    @Transactional
    public boolean save(Student object) {
        try {
            em.persist(object);
            return true;
        } catch (PersistenceException e){
            return false;
        }
    }

    @Override
    public boolean existsById(String id) {
        return em.find(Student.class, id) != null;
    }

    @Override
    public List<String> getAllIds() {
        TypedQuery<String> query = em.createQuery("SELECT s.id FROM Student s", String.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Student> findAllStudentsByCourseName(String courseName) {
        String jpql = """
                SELECT s FROM Student s
                INNER JOIN s.courses c
                WHERE c.name LIKE :courseName
                """;

        TypedQuery<Student> query = em.createQuery(jpql, Student.class);
        query.setParameter("courseName", courseName);
        return query.getResultList();
    }
}

