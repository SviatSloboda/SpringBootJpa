package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseDaoImpl implements CourseDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Course courseToDelete = em.find(Course.class, id);
        em.remove(courseToDelete);

        return em.find(Course.class, id) == null;
    }

    @Override
    @Transactional
    public boolean update(Course object) {
        try {
            em.merge(object);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        try {
            em.createQuery("DELETE FROM Course").executeUpdate();
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    public List<Course> getAll() {
        TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c", Course.class);
        return query.getResultList();
    }

    @Override
    public Optional<Course> getById(String id) {
        return Optional.ofNullable(em.find(Course.class, id));
    }

    @Override
    @Transactional
    public boolean save(Course object) {
        try {
            em.persist(object);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    public boolean existsById(String id) {
        return em.find(Course.class, id) != null;
    }

    @Override
    public List<String> getAllIds() {
        TypedQuery<String> query = em.createQuery("SELECT c.id FROM Course c", String.class);
        return query.getResultList();
    }
}

