package customer.common;

import common.ICrud;
import customer.domain.Customer;

import java.sql.SQLException;
import java.util.List;


public interface ICustomerDao extends ICrud<Customer> {

    List<Customer> getOlderThan(int age) throws SQLException;
    List<Customer> getByName(String name) throws SQLException;
    List<Customer> getByPhone(String phone) throws SQLException;
    List<Customer> getByCity(String city) throws SQLException;
    int deleteByCity(String city) throws SQLException;

}
