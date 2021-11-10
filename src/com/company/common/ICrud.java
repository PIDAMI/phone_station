package com.company.common;

import java.util.List;

public interface ICrud<T>{
    String getTableName();

    // returns negative values in case of error
    Long getTableSize();

    // return id which entry gets in db,so we can set it in java object;
    // also returns negative values in case of error;
    Long create(T entity);
    T get(Long id);
    List<T> getAll();
    Boolean update(T entity);
    Boolean delete(T entity);
    Boolean truncate();
}
