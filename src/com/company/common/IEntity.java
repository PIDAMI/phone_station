package com.company.common;

import java.util.List;

public interface IEntity<T>{
    String getTableName();
    Boolean create(T entity);
    T get(Long id);
    List<T> getAll();
    Boolean update(T entity);
    Boolean delete(T entity);
    Boolean truncate();
}
