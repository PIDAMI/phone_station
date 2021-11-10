package com.company.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Crud<T extends IEntity> implements ICrud<T>{

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



    public Crud(Connection connection, String table,
                List<String> fields, String id_field){
        this.connection=connection;
        this.table=table;
        this.fields = fields;
        this.id_field = id_field;
    }




    private String getStatement(Long id){
        return "SELECT * FROM " + table + " WHERE "
                + id_field + "=?";
    }

    private String createStatement(){
        List<String> quest_marks = new ArrayList<>(fields.size());
        for (int i = 0;i < fields.size();i++)
            quest_marks.add("?");

        return "INSERT INTO " + table + allFieldsAsTuple(fields) +
                " VALUES " + allFieldsAsTuple(quest_marks);
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
        return "DELETE * FROM" + table +
                " WHERE " + id_field + "=?";
    }

    private String truncateStatement(){
        return "DROP TABLE " + table;
    }

    private String getTableSizeStatement(){
        return "SELECT COUNT(*) FROM " + table;
    }

    abstract protected List<T> getEntities(PreparedStatement statement)
            throws SQLException;


    abstract protected void fillStatement(PreparedStatement statement, T entity)
            throws SQLException;


    protected List<T> getByStringField(String query,String field){
        try {
            PreparedStatement statement =
                    connection.prepareStatement(query);
            statement.setString(1,field);
            return getEntities(statement);
        } catch (SQLException e) {
            System.out.println("failed to get entries with value"
                    + field);
            return new ArrayList<>();
        }
    }

    protected List<T> getByIntField(String query,Integer field){
        try {
            PreparedStatement statement =
                    connection.prepareStatement(query);
            statement.setInt(1,field);
            return getEntities(statement);
        } catch (SQLException e) {
            System.out.println("failed to get entries with value"
                    + field);
            return new ArrayList<>();
        }
    }

    @Override
    public String getTableName() {
        return table;
    }

    @Override
    public Long getTableSize(){
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getTableSizeStatement());
            ResultSet rs = statement.executeQuery();
            return rs.getLong(1);

        } catch (SQLException e) {
            System.out.println("failed to count the size of " + table);
            return -1L;
        }
    }

    @Override
    public Long create(T entity) {
        Long id = -1L;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(createStatement());
            fillStatement(statement,entity);
            int res = statement.executeUpdate();
            if (res > 0)
                id = getTableSize();

        } catch (SQLException e) {
            System.out.println("failed to create new entry");
        }
        return id;
    }

    @Override
    public T get(Long id) {
        T res = null;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getStatement(id));
            statement.setLong(1,id);
            List<T> entities = getEntities(statement);
            if (entities.size() > 0)
                 res = entities.get(0);

        } catch (SQLException e) {
            System.out.println("failed to get entry with id "
                    + id);
        }
        return res;
    }

    @Override
    public List<T> getAll() {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getAllStatement());
            return getEntities(statement);

        } catch (SQLException e) {
            System.out.println("failed to get all entries in table " + table);
            return new ArrayList<>();
        }
    }



    @Override
    public Boolean update(T entity) {
        boolean isSuccess = false;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(updateStatement());
            fillStatement(statement,entity);
            statement.setLong(fields.size() + 1,entity.getId());
            int res = statement.executeUpdate();
            isSuccess = res > 0;

        } catch (SQLException e) {
            System.out.println("failed to update entry "
                    + entity.getId());
        }
        return isSuccess;
    }

    @Override
    public Boolean delete(T entity) {
        boolean isSuccess = false;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(deleteStatement());
            statement.setLong(1,entity.getId());
            int res = statement.executeUpdate();
            isSuccess = res > 0;

        } catch (SQLException e) {
            System.out.println("failed to delete entry with id " +
                    + entity.getId());
        }
        return isSuccess;
    }

    @Override
    public Boolean truncate() {
        boolean isSuccess = false;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(truncateStatement());
            int res = statement.executeUpdate();
            isSuccess = res > 0;

        } catch (SQLException e) {
            System.out.println("failed to truncate table "
                    + table);
        }
        return isSuccess;

    }
}
