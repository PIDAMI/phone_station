package com.company.subscriptions.dao;

import com.company.common.Crud;
import com.company.customer.dao.CustomerDao;
import com.company.customer.domain.Customer;
import com.company.service.dao.ServiceDao;
import com.company.service.domain.Service;
import com.company.subscriptions.common.ISubscriptionDao;
import com.company.subscriptions.domain.Subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SubscriptionDao extends Crud<Subscription> implements ISubscriptionDao {

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
        public String toString() {return val;}

        public String toStringWithQM(){return val + "=?";}
    }

    private final String getActiveStatement =
            "SELECT * FROM " + table +
            " WHERE " + Fields.IS_ACTIVE.toString() + "=TRUE";

    private final String deleteInactiveStatement = "DELETE * FROM " + table +
            " WHERE " + Fields.IS_ACTIVE.toString() + "=FALSE";

    private final String getServicesByCustomerStatement =
            "SELECT * FROM " + ServiceDao.table +
            " JOIN " + this.table +
            " ON " + SubscriptionDao.Fields.SERVICE_ID.toString() +
                    "=" + ServiceDao.Fields.ID.toString()+
            " WHERE " + SubscriptionDao.Fields.SERVICE_ID.toStringWithQM();

    private final String getCustomerByServiceStatement =
            "SELECT * FROM " + CustomerDao.table +
            " JOIN " + this.table +
            " ON " + SubscriptionDao.Fields.CUSTOMER_ID.toString() +
                "=" + CustomerDao.Fields.ID.toString()+
            " WHERE " + SubscriptionDao.Fields.CUSTOMER_ID.toStringWithQM();


    private final String getCustomerMonthlyBillStatement =
            "SELECT SUM(" + ServiceDao.Fields.COST.toString() +
                        "*(30 DIV "+ServiceDao.Fields.DURATION.toString()+")" +
                    ") " +
            "FROM " + ServiceDao.table +
            " JOIN " + this.table +
            " ON " + SubscriptionDao.Fields.CUSTOMER_ID.toString() +
                "=" + CustomerDao.Fields.ID.toString() +
            " WHERE " + Fields.CUSTOMER_ID.toStringWithQM();




    public SubscriptionDao(Connection connection, String table,
                           List<String> fields, String id_field) {
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
    protected void fillStatement(PreparedStatement statement, Subscription entity)
            throws SQLException {

        statement.setLong(1,entity.getCustomerId());
        statement.setLong(2,entity.getServiceId());
        statement.setDate(3,entity.getBeginDate());
        statement.setBoolean(4,entity.getActive());
    }

    @Override
    public List<Subscription> getActiveSub() {
        return getByBoolField(getActiveStatement,true);
    }

    @Override
    public int deleteInactive() {
        int res = -1;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(deleteInactiveStatement);
            statement.setBoolean(1,false);
            res = statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("failed to delete inactive subscriptions");
        }
        return res;
    }

    @Override
    public List<Service> getServicesByCustomer(Customer customer) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getServicesByCustomerStatement);
            statement.setLong(1,customer.getId());
            return getServices(statement);
        } catch (SQLException e) {
            System.out.println("failed to get services purchased by customer "
                    + customer.getId());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Customer> getCustomersByService(Service service) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getCustomerByServiceStatement);
            statement.setLong(1,service.getId());
            return getCustomers(statement);
        } catch (SQLException e) {
            System.out.println("failed to get customers who purchased service "
                    + service.getId());
            return new ArrayList<>();
        }
    }

    //TODO
    @Override
    public int getCustomersMonthlyBill(Customer customer) {
        int res = 0;
        try {
            PreparedStatement statement =
                    connection.prepareStatement(getCustomerMonthlyBillStatement);
            statement.setLong(1,customer.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                res = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("failed to get monthly bill of customer with id "
                    + customer.getId());
        }
        return res;
    }
}
