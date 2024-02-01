package ua.foxminded.springbootjdbcapi.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    int deleteById(int id);

    int update(T object);

    List<T> getAll();
    Optional<T> getById(int id);
    int save(T object);
    int saveWithoutId(T object);
}
