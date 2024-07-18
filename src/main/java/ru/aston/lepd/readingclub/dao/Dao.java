package ru.aston.lepd.readingclub.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {

    Optional<E> findById(K id);

    List<E> findAll();

    E save(E entity);

    boolean update(E entity);

    boolean delete(K id);
}
