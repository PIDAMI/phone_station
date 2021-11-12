package com.company.logic;

import com.company.common.IEntity;
import com.company.customer.dao.CustomerDao;
import com.company.customer.domain.Customer;
import com.company.service.dao.ServiceDao;
import com.company.service.domain.Service;
import com.company.subscriptions.dao.SubscriptionDao;
import com.company.subscriptions.domain.Subscription;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class Logic {

    private final ServiceDao serviceDao;
    private final CustomerDao customerDao;
    private final SubscriptionDao subscriptionDao;
    private Connection connection = null;


    // JDBC driver name and database URL
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";


    private final String DB_URL = "jdbc:mysql://localhost:3306/phone_service";

    //  Database credentials
    private final String USER = "phone_service";
    private final String PASS = "password";


    private enum Action{
        ADD_SUBSCRIPTION(1),
        DELETE_INACTIVE_SUBSCRIPTION(2),
        GET_ALL_SERVICE_SUBSCRIBERS(3),
        GET_ALL_CUSTOMERS_SUBSCRIPTIONS(4),
        GET_CUSTOMER_BILL_NEXT_MONTH(5),
        EXIT(6);

        int val;
        Action(int val){this.val=val;}
        static public Action getByInt(int val){
            for (Action a:Action.values()){
                if (a.val == val)
                    return a;
            }
            return null;
        }

    }



    private void getCustomerBillNextMonth() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter customer id : ");
        try {
            Long customerId = Long.parseLong(scanner.next());
            Customer customer = customerDao.get(customerId);
            if (customer == null){
                System.out.println("Customer not found, return to menu");
                return;
            }
            int bill = subscriptionDao.getCustomersMonthlyBill(customer);
            System.out.println("Customer's bill for next month is " + bill);
        } catch (NumberFormatException e){
            System.out.println("id must be a number");
        }
    }


    private void deleteInactive() throws SQLException {
        int deleted = subscriptionDao.deleteInactive();
        System.out.printf("Deleted %d entries\n",deleted);
    }

    private void getAllServiceSubscribers() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter service id : ");
        try {
            Long serviceId = Long.parseLong(scanner.next());
            Service service = serviceDao.get(serviceId);
            if (service == null){
                System.out.println("Service not found, return to menu");
                return;
            }
            List<Customer> customers = subscriptionDao
                    .getCustomersByService(service);
            for (Customer c:customers){
                System.out.println(c);
            }
       } catch (NumberFormatException e){
            System.out.println("id must be a number");
        }

    }



    private void getAllCustomerSubscriptions() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter customer id : ");
        try {
            Long customerId = Long.parseLong(scanner.next());
            Customer customer = customerDao.get(customerId);
            if (customer == null){
                System.out.println("Customer not found, return to menu");
                return;
            }
            List<Service> services = subscriptionDao
                    .getServicesByCustomer(customer);
            for (Service s:services){
                System.out.println(s);
            }
        } catch (NumberFormatException e){
            System.out.println("id must be a number");
        }

    }


    private void addSubToExistingCustomer(Scanner scanner, Long customerId)
            throws SQLException {

//            System.out.println("Enter the action");
            Customer customer = customerDao.get(customerId);
            if (customer == null){
                System.out.println("Not found, registering new customer");
            } else {
                System.out.println("Enter service id: ");
                try{
                    Long serviceId = Long.parseLong(scanner.next());
                    Service service = serviceDao.get(serviceId);
                    if (service == null)
                        System.out.println("Service not found, return to menu");
                    else{
                        subscriptionDao.create(new Subscription(
                                null,customerId,serviceId,null,true));

                    }
                } catch (NumberFormatException e){
                    System.out.println("id must be a number");
                }


            }

    }

    public void addSubscription() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Is subscriber registered? [y/n]: ");
        String isRegistered = scanner.next().toLowerCase();
        if (isRegistered.equals("y")){
            System.out.println("Enter customer id: ");
            Long customerId = Long.getLong(scanner.next()); // TODO: id is null somehow
            addSubToExistingCustomer(scanner, customerId);
        } else if (isRegistered.equals("n")){
            System.out.println("Enter name: ");
            String name = scanner.next();
            System.out.println("Enter age: ");
            Integer age = Integer.getInteger(scanner.next());
            System.out.println("Enter phone: ");
            String phone = scanner.next();
            System.out.println("Enter city: ");
            String city = scanner.next();
            System.out.println("Enter street: ");
            String street = scanner.next();
            Long customerId = customerDao.create(
                    new Customer(null,name,age,phone,city,street)
            );
            addSubToExistingCustomer(scanner,customerId);

        } else {
            System.out.println("this is not \"y\" or \"n\", aborting");
        }
    }



    public boolean processInput(Action action){
        boolean continueProgram = true;
        try {
            switch (action) {
                case ADD_SUBSCRIPTION -> addSubscription();
                case DELETE_INACTIVE_SUBSCRIPTION -> deleteInactive();
                case GET_ALL_SERVICE_SUBSCRIBERS -> getAllServiceSubscribers();
                case GET_ALL_CUSTOMERS_SUBSCRIPTIONS -> getAllCustomerSubscriptions();
                case GET_CUSTOMER_BILL_NEXT_MONTH -> getCustomerBillNextMonth();
                case EXIT -> continueProgram = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return continueProgram;
    }


    Logic() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        serviceDao = new ServiceDao(connection);
        customerDao = new CustomerDao(connection);
        subscriptionDao = new SubscriptionDao(connection);
    }


    public void closeConnection(){
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("error while closing connection");
            }
        }
    }


    public static void main(String[] args) {
        Logic l = null;
        try {
            l = new Logic();
            Scanner scanner = new Scanner(System.in);
            boolean continueRun = true;
            do {
                System.out.println("Enter the command: ");
                Action action = Action.getByInt(scanner.nextInt());
                if (action == null){
                    System.out.println("invalid action symbol");
                    continue;
                }
                continueRun = l.processInput(action);
            } while (continueRun);

        } catch (SQLException throwables) {
            System.out.println("cant connect to db");
        } catch (InputMismatchException e) {
            System.out.println("invalid action symbol");
        } catch (ClassNotFoundException e) {
            System.out.println("driver class not found");
        } finally {
            if (l != null)
                l.closeConnection();
        }


//        try{
//            Logic l = new Logic();
//
//
//
//
//        } catch (SQLException throwables) {
//            System.out.println("sql error");
//            throwables.printStackTrace();
//        } catch (ClassNotFoundException e){
//            System.out.println("class not found");
//            e.printStackTrace();
//        }
    }


}
