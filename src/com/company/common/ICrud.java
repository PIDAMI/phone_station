package com.company.common;

import java.util.List;

public interface ICrud<T extends IEntity>{
    String getTableName();
    Boolean create(T entity);
    T get(Long id);
    List<T> getAll();
    Boolean update(T entity);
    Boolean delete(T entity);
    Boolean truncate();
}
