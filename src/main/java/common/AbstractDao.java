package common;

import domain.IEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


// template class with implementation of methods common to every dao
public abstract class AbstractDao<T extends IEntity>{

    private final String table;
    private final List<String> fields; // names of fields except for id
    private final String id_field; // name of id field
    protected final Connection connection;

    public String allFieldsWithQM(){
        return  fields.stream()
                .map(str -> str + "=? ")
                .reduce("",(String x, String y)->x+y);
    }

    public String allFieldsAsTuple(List<String> fields){
        StringBuilder s = new StringBuilder();
        s.append("(");
        for(String field: fields){
            s.append(field);
            s.append(",");
        }
        s.deleteCharAt(s.length()-1);
        s.append(")");
        return s.toString();
    }



    public AbstractDao(Connection connection, String table,
                       List<String> fields, String id_field){
        this.connection=connection;
        this.table=table;
        this.fields = fields;
        this.id_field = id_field;
    }




    private String getStatement(){
        return "SELECT * FROM " + table + " WHERE "
                + id_field + "=?";
    }

    private String createStatement(){
        List<String> quest_marks = new ArrayList<>(fields.size());
        for (int i = 0;i < fields.size();i++)
            quest_marks.add("?");

        return "INSERT INTO " + table + allFieldsAsTuple(fields) +
                " VALUE " + allFieldsAsTuple(quest_marks);
    }

    private String getAllStatement(){
        return "SELECT * FROM " + table;
    }

    private String updateStatement(){
        return "UPDATE " + table +
                " SET " + allFieldsWithQM() +
                " WHERE " + id_field + "=?";
    }

    private String deleteStatement(){
        return "DELETE FROM" + table +
                " WHERE " + id_field + "=?";
    }

    private String truncateStatement(){
        return "DROP TABLE " + table;
    }

    private String getTableSizeStatement(){
        return "SELECT MAX("+id_field+") FROM " + table;
    }

    abstract protected List<T> getEntities(PreparedStatement statement)
            throws SQLException;


    abstract protected void fillStatement(PreparedStatement statement, T entity)
            throws SQLException;


    protected List<T> getByStringField(String query,String field)
            throws SQLException{

        PreparedStatement statement =
                connection.prepareStatement(query);
        statement.setString(1,field);
        return getEntities(statement);

    }

    protected List<T> getByIntField(String query,Integer field)
        throws  SQLException{

        PreparedStatement statement =
                connection.prepareStatement(query);
        statement.setInt(1,field);
        return getEntities(statement);

    }

    protected List<T> getByBoolField(String query,Boolean field)
            throws SQLException{

        PreparedStatement statement =
                connection.prepareStatement(query);
        statement.setBoolean(1,field);
        return getEntities(statement);

    }




    public String getTableName() {
        return table;
    }


    public Long getTableSize() throws SQLException {

        PreparedStatement statement =
                connection.prepareStatement(getTableSizeStatement());
        ResultSet rs = statement.executeQuery();
        if (!rs.next())
            throw new SQLException("can't get table size");
        return rs.getLong(1);


    }


    public Long create(T entity) throws SQLException {
        Long id = 0L;
        PreparedStatement statement =
                connection.prepareStatement(createStatement());
        fillStatement(statement,entity);
        int res = statement.executeUpdate();
        if (res > 0)
            id = getTableSize();

        return id;
    }


    public T get(Long id) throws SQLException {
        T res = null;

        PreparedStatement statement =
                connection.prepareStatement(getStatement());
        statement.setLong(1,id);
        List<T> entities = getEntities(statement);
        if (entities.size() > 0)
             res = entities.get(0);


        return res;
    }


    public List<T> getAll() throws SQLException {

        PreparedStatement statement =
                connection.prepareStatement(getAllStatement());
        return getEntities(statement);


    }




    public Boolean update(T entity) throws SQLException {
        boolean isSuccess = false;
        PreparedStatement statement =
                connection.prepareStatement(updateStatement());
        fillStatement(statement,entity);
        statement.setLong(fields.size() + 1,entity.getId());
        int res = statement.executeUpdate();
        isSuccess = res > 0;

        return isSuccess;
    }


    public Boolean delete(T entity) throws SQLException {
        boolean isSuccess = false;

        PreparedStatement statement =
                connection.prepareStatement(deleteStatement());
        statement.setLong(1,entity.getId());
        int res = statement.executeUpdate();
        isSuccess = res > 0;

        return isSuccess;
    }


    public Boolean truncate() throws SQLException {
        boolean isSuccess = false;

        PreparedStatement statement =
                connection.prepareStatement(truncateStatement());
        int res = statement.executeUpdate();
        isSuccess = res > 0;


        return isSuccess;

    }
}
