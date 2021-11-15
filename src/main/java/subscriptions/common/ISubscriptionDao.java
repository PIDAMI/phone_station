package subscriptions.common;

import common.ICrud;
import customer.domain.Customer;
import service.domain.Service;
import subscriptions.domain.Subscription;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ISubscriptionDao extends ICrud<Subscription> {

    List<Subscription> getActiveSub() throws SQLException;
    int deleteInactive() throws SQLException; // return num of deleted entries
    List<Service> getServicesByCustomer(Customer customer) throws SQLException;
    List<Customer> getCustomersByService(Service service) throws SQLException;

    // get amount of money customer has to pay in following month from now
    long getCustomersMonthlyBill(Customer customer) throws SQLException;


}
