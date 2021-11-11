package com.company.subscriptions.common;

import com.company.common.ICrud;
import com.company.customer.domain.Customer;
import com.company.service.domain.Service;
import com.company.subscriptions.domain.Subscription;

import java.util.Date;
import java.util.List;

public interface ISubscriptionDao extends ICrud<Subscription> {

    List<Subscription> getActiveSub();
    int deleteInactive(); // return num of deleted entries
    List<Service> getServicesByCustomer(Customer customer);
    List<Customer> getCustomersByService(Service service);

    // get amount of money customer has to pay in following month from now
    int getCustomersMonthlyBill(Customer customer);


}
