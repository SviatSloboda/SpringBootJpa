package ua.foxminded.springbootjdbcapi.dao.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.model.Group;

import java.util.List;
import java.util.Optional;

@Repository
public class GroupDaoImpl implements GroupDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Group group = em.find(Group.class, id);
        try {
            em.remove(group);
            return em.find(Group.class, id) == null;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public boolean update(Group object) {
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
            em.createQuery("DELETE FROM Group").executeUpdate();
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    public List<Group> getAll() {
        TypedQuery<Group> query = em.createQuery("SELECT p FROM Group p", Group.class);
        return query.getResultList();
    }

    @Override
    public Optional<Group> getById(String id) {
        return Optional.ofNullable(em.find(Group.class, id));
    }

    @Transactional
    @Override
    public boolean save(Group object) {
        em.persist(object);

        Group group = em.find(Group.class, object.getId());

        return group != null;
    }

    @Override
    public boolean existsById(String id) {
        return em.find(Group.class, id) != null;
    }

    @Override
    @Transactional
    public List<Group> findAllGroupsWithLessOrEqualStudentsNumber(int studentNumber) {
        String jpql = """
                SELECT g
                FROM Group g
                LEFT JOIN g.students s
                GROUP BY g.id
                HAVING COUNT(s.id) <= :studentNumber
                """;

        TypedQuery<Group> query = em.createQuery(jpql, Group.class);
        query.setParameter("studentNumber", studentNumber);

        return query.getResultList();
    }

    @Override
    public List<String> getAllIds() {
        TypedQuery<String> query = em.createQuery("SELECT g.id FROM Group g", String.class);
        return query.getResultList();
    }
}
