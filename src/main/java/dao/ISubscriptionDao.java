package dao;

import domain.Customer;
import domain.Service;
import domain.Subscription;

import java.sql.SQLException;
import java.util.List;

public interface ISubscriptionDao{

    List<Subscription> getActiveSub() throws SQLException;
    int deleteInactive() throws SQLException; // return num of deleted entries
    List<Service> getServicesByCustomer(Customer customer) throws SQLException;
    List<Customer> getCustomersByService(Service service) throws SQLException;

    // get amount of money customer has to pay in following month from now
    long getCustomersMonthlyBill(Customer customer) throws SQLException;


}
