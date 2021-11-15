package common;
import java.sql.SQLException;
import java.util.List;

public interface ICrud<T>{
    String getTableName();

    // returns negative values in case of error
    Long getTableSize()throws SQLException;

    // return id which entry gets in db,so we can set it in java object;
    // also returns negative values in case of error;
    Long create(T entity) throws SQLException;
    T get(Long id)throws SQLException;
    List<T> getAll()throws SQLException;
    Boolean update(T entity)throws SQLException;
    Boolean delete(T entity)throws SQLException;
    Boolean truncate()throws SQLException;
}
