package com.company.subscriber.dao;

import com.company.subscriber.common.ISubscriberDao;
import com.company.subscriber.domain.Subscriber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SubscriberDao implements ISubscriberDao {

    private enum Fields{
        ID("id"),
        NAME("name"),
        AGE("age"),
        PHONE("phone"),
        CITY("city"),
        STREET("street");

        private final String val;
        Fields(String val){this.val=val;}

        @Override
        public String toString() {return val;}

        // return (name,...)
        public static String asTupleNoId(){
            StringBuilder s = new StringBuilder();
            s.append("(");
            for(Fields f:Fields.values()){
                s.append(f.toString());
                s.append(",");
            }
            s.deleteCharAt(s.length()-1);
            s.append(")");
            return s.toString();
        }
        public String toStringWithQM(){
            return toString() + "=?";
        }

        public static String toStringAllWithQM(){
            return Arrays.stream(Fields.values())
                    .map(Fields::toString)
                    .map(str -> str + "=? ")
                    .reduce("",(String x, String y)->x+y);
        }

    }


    private final String table = "subscriber";

    private final String createStatement =
            "INSERT INTO " + table + Fields.asTupleNoId() +
                    " VALUES (?,?,?,?,?)";

    private final String getStatement =
            "SELECT * FROM " + table + " WHERE "
                    + Fields.ID.toStringWithQM();

    private final String getAllStatement =
            "SELECT * FROM " + table;

    private final String updateStatement =
            "UPDATE " + table +
            " SET " + Fields.toStringAllWithQM() +
            " WHERE " + Fields.ID.toStringWithQM();

    private final String deleteStatement =
            "DELETE * FROM" + table +
            " WHERE " + Fields.ID.toStringWithQM();

    private final String truncateStatement = "DROP TABLE " + table;

    private final String getOlderThanStatement = "SELECT * FROM " + table +
            "WHERE " + Fields.AGE.toString() + " >?";

    private final String getByNameStatement = "SELECT * FROM " + table +
            "WHERE " + Fields.NAME.toStringWithQM();

    private final String getByPhoneStatement = "SELECT * FROM " + table +
            "WHERE " + Fields.PHONE.toStringWithQM();

    private final String getByCityStatement = "SELECT * FROM " + table +
            "WHERE " + Fields.CITY.toStringWithQM();

    private final String deleteByCityStatement = "DELETE * FROM " + table +
            "WHERE " + Fields.CITY.toStringWithQM();





    private final Connection connection;


    public SubscriberDao(Connection connection) {
        this.connection = connection;
    }

    private List<Subscriber> getSubscribers(PreparedStatement statement)
            throws SQLException {

        List<Subscriber> subscribers = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            subscribers.add(new Subscriber(
                    rs.getLong(Fields.ID.toString()),
                    rs.getString(Fields.NAME.toString()),
                    rs.getInt(Fields.AGE.toString()),
                    rs.getString(Fields.PHONE.toString()),
                    rs.getString(Fields.CITY.toString()),
                    rs.getString(Fields.STREET.toString()))
            );
        }
        return subscribers;
    }



    private void fillStatement(PreparedStatement statement, Subscriber entity)
            throws SQLException {
        statement.setString(1,entity.getName());
        statement.setInt(2,entity.getAge());
        statement.setString(3,entity.getPhone());
        statement.setString(4,entity.getCity());
        statement.setString(5,entity.getStreet());
    }


    @Override
    public String getTableName() {
        return table;
    }




    @Override
    public Boolean create(Subscriber entity) {
        boolean isSuccess = false;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(createStatement);
            fillStatement(statement,entity);
            int res = statement.executeUpdate();
            isSuccess = res > 0;

        } catch (SQLException e) {
            System.out.println("failed to create new subscriber " + e.getMessage());
        }
        return isSuccess;
    }

    @Override
    public Subscriber get(Long id) {
        Subscriber subscriber = null;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getStatement);
            statement.setLong(1,id);
            List<Subscriber> res = getSubscribers(statement);
            if (res.size() > 0)
                subscriber = res.get(0);

        } catch (SQLException e) {
            System.out.println("failed to get subscriber with id "
                    + id);
        }

        return subscriber;
    }

    @Override
    public List<Subscriber> getAll() {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getAllStatement);
            return getSubscribers(statement);

        } catch (SQLException e) {
            System.out.println("failed to get all subscribers");
            return new ArrayList<>();
        }
    }


    @Override
    public Boolean update(Subscriber entity) {
        boolean isSuccess = false;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(updateStatement);
            fillStatement(statement,entity);
            statement.setLong(6,entity.getId());
            int res = statement.executeUpdate();
            isSuccess = res > 0;

        } catch (SQLException e) {
            System.out.println("failed to update subscriber "
                    + entity.getId());
        }
        return isSuccess;
    }

    @Override
    public Boolean delete(Subscriber entity) {
        boolean isSuccess = false;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(deleteStatement);
            statement.setLong(1,entity.getId());
            int res = statement.executeUpdate();
            isSuccess = res > 0;

        } catch (SQLException e) {
            System.out.println("failed to delete subscriber "
                    + entity.getId());
        }
        return isSuccess;
    }

    @Override
    public Boolean truncate() {
        boolean isSuccess = false;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(truncateStatement);
            int res = statement.executeUpdate();
            isSuccess = res > 0;

        } catch (SQLException e) {
            System.out.println("failed to truncate table "
                    + table);
        }
        return isSuccess;

    }



    @Override
    public List<Subscriber> getOlderThan(int age) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getOlderThanStatement);
            statement.setInt(1,age);
            return getSubscribers(statement);

        } catch (SQLException e) {
            System.out.println("failed to get subscribers older than " + age);
            return new ArrayList<>();
        }
    }


    @Override
    public List<Subscriber> getByName(String name) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getByNameStatement);
            statement.setString(1,name);
            return getSubscribers(statement);
        } catch (SQLException e) {
            System.out.println("failed to get subscribers with name "
                    + name);
            return new ArrayList<>();
        }

    }

    @Override
    public List<Subscriber> getByPhone(String phone) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getByPhoneStatement);
            statement.setString(1,phone);
            return getSubscribers(statement);
        } catch (SQLException e) {
            System.out.println("failed to get subscribers with phone "
                    + phone);
            return new ArrayList<>();
        }

    }

    @Override
    public List<Subscriber> getByCity(String city) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getByCityStatement);
            statement.setString(1,city);
            return getSubscribers(statement);
        } catch (SQLException e) {
            System.out.println("failed to get subscriber with city "
                    + city);
            return new ArrayList<>();
        }

    }



    @Override
    public int deleteByCity(String city) {
        int deleted = 0;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(deleteByCityStatement);
            statement.setString(1,city);
            deleted = statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("failed to delete subscriber with city"
                    + city);
        }

        return deleted;
    }
}
