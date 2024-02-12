package ua.foxminded.springbootjdbcapi.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    boolean deleteById(String id);

    boolean update(T object);
    boolean deleteAll();

    List<T> getAll();
    Optional<T> getById(String id);
    boolean save(T object);
    boolean existsById(String id);
    List<String> getAllIds();
}
