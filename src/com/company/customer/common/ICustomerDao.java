package com.company.customer.common;

import com.company.common.ICrud;
import com.company.customer.domain.Customer;

import java.util.List;


public interface ICustomerDao extends ICrud<Customer> {

    List<Customer> getOlderThan(int age);
    List<Customer> getByName(String name);
    List<Customer> getByPhone(String phone);
    List<Customer> getByCity(String city);
    int deleteByCity(String city);

}
