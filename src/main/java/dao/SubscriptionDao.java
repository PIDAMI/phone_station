package dao;

import common.AbstractDao;
import domain.Customer;
import domain.Service;
import domain.Subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubscriptionDao extends AbstractDao<Subscription> implements ISubscriptionDao {

    public static final String table = "subscription";
    public enum Fields{
        ID("id"),
        CUSTOMER_ID("c_id"),
        SERVICE_ID("s_id"),
        BEGINNING_DATE("beginning_date"),
        IS_ACTIVE("is_active");

        private final String val; // name of field in db
        Fields(String val){this.val=val;}

        @Override
        public String toString() {return table + "." + val;}

        public String toStringWithQM(){return toString() + "=?";}
    }

    private final String getActiveStatement =
            "SELECT * FROM " + table +
            " WHERE " + Fields.IS_ACTIVE.toString() + "=TRUE";

    private final String deleteInactiveStatement =
            "DELETE FROM " + table +
            " WHERE " + Fields.IS_ACTIVE.toString() + "=FALSE";

    private final String getServicesByCustomerStatement =
            "SELECT * FROM " + ServiceDao.table +
            " JOIN " + this.table +
            " ON " + SubscriptionDao.Fields.SERVICE_ID.toString() +
                    "=" + ServiceDao.Fields.ID.toString()+
            " WHERE " + SubscriptionDao.Fields.CUSTOMER_ID.toStringWithQM();

    private final String getCustomerByServiceStatement =
            "SELECT * FROM " + CustomerDao.table +
            " JOIN " + this.table +
            " ON " + SubscriptionDao.Fields.CUSTOMER_ID.toString() +
                "=" + CustomerDao.Fields.ID.toString()+
            " WHERE " + SubscriptionDao.Fields.SERVICE_ID.toStringWithQM();


    private final String getCustomerMonthlyBillStatement =
            "SELECT SUM(" + ServiceDao.Fields.COST.toString() +
                        "*(30 DIV "+ServiceDao.Fields.DURATION.toString()+")" +
                    ") " +
            "FROM " + ServiceDao.table +
            " JOIN " + this.table +
            " ON " + Fields.SERVICE_ID.toString() +
                "=" + ServiceDao.Fields.ID.toString() +
            " WHERE " + Fields.CUSTOMER_ID.toStringWithQM();
//    private final String getCustomerMonthlyBillStatement =
//        "select sum(service.cost * (30 DIV duration_days)) sum\n" +
//        "    from service\n" +
//        "        join subscription\n" +
//        "            on service.id = subscription.s_id\n" +
//        "            where c_id=?;";



    public SubscriptionDao(Connection connection) {
        super(connection,"subscription",
                Arrays.stream(SubscriptionDao.Fields.values())
                        .filter(w->!w.equals(SubscriptionDao.Fields.ID))
                        .map(SubscriptionDao.Fields::toString).toList(),
                SubscriptionDao.Fields.ID.toString());
    }


    private List<Service> getServices(PreparedStatement statement)
        throws SQLException {


            List<Service> services = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                services.add(new Service(
                        rs.getLong(ServiceDao.Fields.ID.toString()),
                        rs.getString(ServiceDao.Fields.TITLE.toString()),
                        rs.getInt(ServiceDao.Fields.COST.toString()),
                        rs.getInt(ServiceDao.Fields.DURATION.toString()))
                );
            }
            return services;
    }

    private List<Customer> getCustomers(PreparedStatement statement)
            throws SQLException {

        List<Customer> customers = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            customers.add(new Customer(
                    rs.getLong(CustomerDao.Fields.ID.toString()),
                    rs.getString(CustomerDao.Fields.NAME.toString()),
                    rs.getInt(CustomerDao.Fields.AGE.toString()),
                    rs.getString(CustomerDao.Fields.PHONE.toString()),
                    rs.getString(CustomerDao.Fields.CITY.toString()),
                    rs.getString(CustomerDao.Fields.STREET.toString()))
            );
        }
        return customers;
    }

    @Override
    protected List<Subscription> getEntities(PreparedStatement statement)
            throws SQLException {

        List<Subscription> subscriptions = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            subscriptions.add(new Subscription(
                    rs.getLong(SubscriptionDao.Fields.ID.toString()),
                    rs.getLong(SubscriptionDao.Fields.SERVICE_ID.toString()),
                    rs.getLong(SubscriptionDao.Fields.CUSTOMER_ID.toString()),
                    rs.getDate(SubscriptionDao.Fields.BEGINNING_DATE.toString()),
                    rs.getBoolean(SubscriptionDao.Fields.IS_ACTIVE.toString()))
            );
        }
        return subscriptions;
    }

    @Override
    protected void fillStatement(PreparedStatement statement,
                                 Subscription entity)
            throws SQLException {

        statement.setLong(1,entity.getCustomerId());
        statement.setLong(2,entity.getServiceId());
        statement.setDate(3,entity.getBeginDate());
        statement.setBoolean(4,entity.getActive());
    }

    @Override
    public List<Subscription> getActiveSub() throws SQLException {
        return getByBoolField(getActiveStatement,true);
    }

    @Override
    public int deleteInactive() throws SQLException {
        int res = -1;
        PreparedStatement statement =
                connection.prepareStatement(deleteInactiveStatement);
        res = statement.executeUpdate();

        return res;
    }

    @Override
    public List<Service> getServicesByCustomer(Customer customer)
            throws SQLException {

        PreparedStatement statement =
                connection.prepareStatement(getServicesByCustomerStatement);
        statement.setLong(1,customer.getId());
        return getServices(statement);

    }

    @Override
    public List<Customer> getCustomersByService(Service service)
            throws SQLException {

        PreparedStatement statement =
                connection.prepareStatement(getCustomerByServiceStatement);
        statement.setLong(1,service.getId());
        return getCustomers(statement);

    }

    //TODO
    @Override
    public long getCustomersMonthlyBill(Customer customer)
            throws SQLException {
        long res = 0;
        PreparedStatement statement =
                connection.prepareStatement(getCustomerMonthlyBillStatement);
        statement.setLong(1,customer.getId());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            res = resultSet.getLong(1);
        }
        else
            throw new SQLException("cant get customer bill");

        return res;
    }
}
