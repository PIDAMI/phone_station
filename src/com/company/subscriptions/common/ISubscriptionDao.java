package com.company.subscriptions.common;

import com.company.common.ICrud;
import com.company.customer.domain.Customer;
import com.company.service.domain.Service;
import com.company.subscriptions.domain.Subscription;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ISubscriptionDao extends ICrud<Subscription> {

    List<Subscription> getActiveSub() throws SQLException;
    int deleteInactive() throws SQLException; // return num of deleted entries
    List<Service> getServicesByCustomer(Customer customer) throws SQLException;
    List<Customer> getCustomersByService(Service service) throws SQLException;

    // get amount of money customer has to pay in following month from now
    int getCustomersMonthlyBill(Customer customer) throws SQLException;


}
