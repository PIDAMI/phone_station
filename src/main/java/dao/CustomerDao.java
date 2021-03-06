package dao;

import common.AbstractDao;
import domain.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CustomerDao extends AbstractDao<Customer> implements ICustomerDao {

    public enum Fields{
        ID("id"),
        NAME("name"),
        AGE("age"),
        PHONE("phone"),
        CITY("city"),
        STREET("street");

        private final String val; // name of field in db
        Fields(String val){this.val=val;}

        @Override
        public String toString() {return table + "." + val;}

        public String toStringWithQM(){return toString() + "=?";}
    }


    public static final String table = "customer";


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



    public CustomerDao(Connection connection) {
        super(connection,"customer",
                Arrays.stream(Fields.values())
                        .filter(w->!w.equals(Fields.ID))
                        .map(Fields::toString).toList(),
                Fields.ID.toString());
    }

    @Override
    protected List<Customer> getEntities(PreparedStatement statement)
            throws SQLException {

        List<Customer> customers = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            customers.add(new Customer(
                    rs.getLong(Fields.ID.toString()),
                    rs.getString(Fields.NAME.toString()),
                    rs.getInt(Fields.AGE.toString()),
                    rs.getString(Fields.PHONE.toString()),
                    rs.getString(Fields.CITY.toString()),
                    rs.getString(Fields.STREET.toString()))
            );
        }
        return customers;
    }



    @Override
    protected void fillStatement(PreparedStatement statement, Customer entity)
            throws SQLException {
        statement.setString(1,entity.getName());
        statement.setInt(2,entity.getAge());
        statement.setString(3,entity.getPhone());
        statement.setString(4,entity.getCity());
        statement.setString(5,entity.getStreet());
    }


    @Override
    public List<Customer> getOlderThan(int age) throws SQLException {

        PreparedStatement statement =
                connection.prepareStatement(getOlderThanStatement);
        statement.setInt(1,age);
        return getEntities(statement);


    }


    @Override
    public List<Customer> getByName(String name) throws SQLException {
        return getByStringField(getByNameStatement,name);
    }

    @Override
    public List<Customer> getByPhone(String phone) throws SQLException {

        PreparedStatement statement =
                connection.prepareStatement(getByPhoneStatement);
        statement.setString(1,phone);
        return getEntities(statement);


    }

    @Override
    public List<Customer> getByCity(String city) throws SQLException {

        PreparedStatement statement =
                connection.prepareStatement(getByCityStatement);
        statement.setString(1,city);
        return getEntities(statement);


    }



    @Override
    public int deleteByCity(String city) throws SQLException {
        int deleted = 0;

        PreparedStatement statement =
                connection.prepareStatement(deleteByCityStatement);
        statement.setString(1,city);
        deleted = statement.executeUpdate();


        return deleted;
    }


    //
//    private final String createStatement =
//            "INSERT INTO " + table + Fields.asTupleNoId() +
//                    " VALUES (?,?,?,?,?)";
//
//    private final String getStatement =
//            "SELECT * FROM " + table + " WHERE "
//                    + Fields.ID.toStringWithQM();
//
//    private final String getAllStatement =
//            "SELECT * FROM " + table;
//
//    private final String updateStatement =
//            "UPDATE " + table +
//            " SET " + Fields.toStringAllWithQM() +
//            " WHERE " + Fields.ID.toStringWithQM();
//
//    private final String deleteStatement =
//            "DELETE * FROM" + table +
//            " WHERE " + Fields.ID.toStringWithQM();
//
//    private final String truncateStatement = "DROP TABLE " + table;


//    @Override
//    public String getTableName() {
//        return table;
//    }
//
//
//
//
//    @Override
//    public Boolean create(Subscriber entity) {
//        boolean isSuccess = false;
//        try {
//            PreparedStatement statement =
//                    connection.prepareStatement(createStatement);
//            fillStatement(statement,entity);
//            int res = statement.executeUpdate();
//            isSuccess = res > 0;
//
//        } catch (SQLException e) {
//            System.out.println("failed to create new subscriber " + e.getMessage());
//        }
//        return isSuccess;
//    }
//
//    @Override
//    public Subscriber get(Long id) {
//        Subscriber subscriber = null;
//        try {
//            PreparedStatement statement =
//                    connection.prepareStatement(getStatement);
//            statement.setLong(1,id);
//            List<Subscriber> res = getSubscribers(statement);
//            if (res.size() > 0)
//                subscriber = res.get(0);
//
//        } catch (SQLException e) {
//            System.out.println("failed to get subscriber with id "
//                    + id);
//        }
//
//        return subscriber;
//    }
//
//    @Override
//    public List<Subscriber> getAll() {
//        try {
//            PreparedStatement statement =
//                    connection.prepareStatement(getAllStatement);
//            return getSubscribers(statement);
//
//        } catch (SQLException e) {
//            System.out.println("failed to get all subscribers");
//            return new ArrayList<>();
//        }
//    }
//
//
//    @Override
//    public Boolean update(Subscriber entity) {
//        boolean isSuccess = false;
//        try {
//            PreparedStatement statement =
//                    connection.prepareStatement(updateStatement);
//            fillStatement(statement,entity);
//            statement.setLong(6,entity.getId());
//            int res = statement.executeUpdate();
//            isSuccess = res > 0;
//
//        } catch (SQLException e) {
//            System.out.println("failed to update subscriber "
//                    + entity.getId());
//        }
//        return isSuccess;
//    }
//
//    @Override
//    public Boolean delete(Subscriber entity) {
//        boolean isSuccess = false;
//        try {
//            PreparedStatement statement =
//                    connection.prepareStatement(deleteStatement);
//            statement.setLong(1,entity.getId());
//            int res = statement.executeUpdate();
//            isSuccess = res > 0;
//
//        } catch (SQLException e) {
//            System.out.println("failed to delete subscriber "
//                    + entity.getId());
//        }
//        return isSuccess;
//    }
//
//    @Override
//    public Boolean truncate() {
//        boolean isSuccess = false;
//        try {
//            PreparedStatement statement =
//                    connection.prepareStatement(truncateStatement);
//            int res = statement.executeUpdate();
//            isSuccess = res > 0;
//
//        } catch (SQLException e) {
//            System.out.println("failed to truncate table "
//                    + table);
//        }
//        return isSuccess;
//
//    }



}
