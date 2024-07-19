package com.project.house.rental.service;

import java.util.List;

public interface GenericService<T, V> {
    List<V> getAll();

    V getById(long id);

    V create(V v);

    V update(long id, V v);

    void deleteById(long id);

    V toDto(T t);

    T toEntity(V v);

    void updateEntityFromDto(T t, V v);
}
