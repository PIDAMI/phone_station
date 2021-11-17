package logic;

import domain.IEntity;
import dao.CustomerDao;
import domain.Customer;
import dao.ServiceDao;
import domain.Service;
import dao.SubscriptionDao;
import domain.Subscription;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Logic {

    private final ServiceDao serviceDao;
    private final CustomerDao customerDao;
    private final SubscriptionDao subscriptionDao;
    private Connection connection = null;


    // JDBC driver name and database URL
    private String JDBC_DRIVER ;


    private String DB_URL ;

    //  Database credentials
    private String USER;
    private String PASS;


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



    private String getCustomerBillNextMonth(Scanner scanner) throws SQLException {
        String message = "";
        System.out.println("Enter customer id : ");
        try {
            Long customerId = Long.parseLong(scanner.next());
            Customer customer = customerDao.get(customerId);
            if (customer == null){
                return "Customer not found, return to menu";
            }
            System.out.println(customer);
            long bill = subscriptionDao.getCustomersMonthlyBill(customer);
            message = "Customer's bill for next month is " + bill;
        } catch (NumberFormatException e){
            message =  "Id must be a number";
        }
        return message;
    }


    private String deleteInactive() throws SQLException {
        int deleted = subscriptionDao.deleteInactive();
        return "Deleted " + deleted + " entries";
    }


    private String getAllServiceSubscribers(Scanner scanner) throws SQLException {
        String message = "";
        System.out.println("Enter service id : ");
        try {
            Long serviceId = Long.parseLong(scanner.next());
            Service service = serviceDao.get(serviceId);
            if (service == null){
                return "Service not found, return to menu";
            }
            List<Customer> customers = subscriptionDao
                    .getCustomersByService(service);
            message = customers
                    .stream()
                    .map(Customer::toString)
                    .collect( Collectors.joining( "\n" ) );
       } catch (NumberFormatException e){
            message = "id must be a number";
        }
        return message;
    }



    private String getAllCustomerSubscriptions(Scanner scanner) throws SQLException {
        String message = "";
        System.out.println("Enter customer id : ");
        try {
            Long customerId = Long.parseLong(scanner.next());
            Customer customer = customerDao.get(customerId);
            if (customer == null){
                return "Customer not found, return to menu";
            }
            List<Service> services = subscriptionDao
                    .getServicesByCustomer(customer);
            message = services
                    .stream()
                    .map(Service::toString)
                    .collect( Collectors.joining( "\n" ) );
        } catch (NumberFormatException e){
            message = "Id must be a number";
        }
        return message;
    }



    private boolean isCustomerSubscribed(Customer customer, Service service)
            throws SQLException {
        List<Service> servicesList = subscriptionDao
                .getServicesByCustomer(customer);

        return servicesList.contains(service);
    }

    private String addSubToExistingCustomer(Scanner scanner, Long customerId)
            throws SQLException {
            String message = "";
            Customer customer = customerDao.get(customerId);
            if (customer == null){
                message = "Not found, return to menu";
            } else {
                System.out.println("Enter service id: ");
                try{
                    Long serviceId = Long.parseLong(scanner.next());
                    Service service = serviceDao.get(serviceId);
                    if (service == null){
                        return "Service not found, return to menu";

                    }
                    if (isCustomerSubscribed(customer,service)){
                        return "Customer is already subscribed to this service";
                    }
                    subscriptionDao.create(new Subscription(
                            null,
                            customerId,
                            serviceId,
                            new Date(System.currentTimeMillis()),
                            true));
                    message = "Subscription added";

                } catch (NumberFormatException e){
                    message = "Id must be a number";
                }


            }
            return message;

    }

    public String addSubscription(Scanner scanner) throws SQLException {
        String message = "";
        System.out.println("Is subscriber registered? [y/n]: ");
        String isRegistered = scanner.next().toLowerCase();
        try{
            if (isRegistered.equals("y")){
                System.out.println("Enter customer id: ");
                Long customerId = Long.parseLong(scanner.next());
                message = addSubToExistingCustomer(scanner, customerId);
            } else if (isRegistered.equals("n")){
                System.out.println("Enter name: ");
                String name = scanner.next();
                System.out.println("Enter age: ");
                Integer age = Integer.parseInt(scanner.next());
                System.out.println("Enter phone: ");
                String phone = scanner.next();
                System.out.println("Enter city: ");
                String city = scanner.next();
                System.out.println("Enter street: ");
                String street = scanner.next();
                Long customerId = customerDao.create(
                        new Customer(null,name,age,phone,city,street)
                );
                message = addSubToExistingCustomer(scanner,customerId);

            } else {
                message = "this is not \"y\" or \"n\", aborting";
            }
        } catch (NumberFormatException e) {
            message = "id must be an integer";
        }
        return message;

    }



    public boolean processInput(Scanner scanner, Action action){
        boolean continueProgram = true;
        try {
            switch (action) {
                case ADD_SUBSCRIPTION -> System.out.println(addSubscription(scanner));
                case DELETE_INACTIVE_SUBSCRIPTION -> System.out.println(deleteInactive());
                case GET_ALL_SERVICE_SUBSCRIBERS -> System.out.println(getAllServiceSubscribers(scanner));
                case GET_ALL_CUSTOMERS_SUBSCRIPTIONS -> System.out.println(getAllCustomerSubscriptions(scanner));
                case GET_CUSTOMER_BILL_NEXT_MONTH -> System.out.println(getCustomerBillNextMonth(scanner));
                case EXIT -> continueProgram = false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return continueProgram;
    }




    void setParameters(String propertiesFile) throws FileNotFoundException {
        Config config = new Config(List.of("JDBC_DRIVER", "DB_URL", "USER", "PASS"));
        config.ParseConfig(propertiesFile);
        HashMap<String,String> params = config.getParams();
        for (String key: params.keySet()){
            String value = params.get(key);
            switch (key){
                case "JDBC_DRIVER":
                    JDBC_DRIVER = value;
                    break;
                case "DB_URL":
                    DB_URL = value;
                    break;
                case "USER":
                    USER = value;
                    break;
                case "PASS":
                    PASS = value;
                    break;
            }

        }

    }

    private Logic(String propertiesFile)
            throws SQLException, ClassNotFoundException, FileNotFoundException {

        setParameters(propertiesFile);

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
            l = new Logic("src/main/resources/.properties");
            Scanner scanner = new Scanner(System.in);
            boolean continueRun = true;
            System.out.println("Enter number of command:\n" +
                    "1 - add subscription\n" +
                    "2 - delete inactive subscriptions\n" +
                    "3 - get all customers, subscribed to given service\n" +
                    "4 - get all services given customer is subscribed to\n" +
                    "5 - get sum of money given customer has to pay in 1 month\n" +
                    "6 - exit program");

            do {
                System.out.println("Enter the command: ");
                Action action = Action.getByInt(scanner.nextInt());
                if (action == null){
                    System.out.println("invalid action symbol");
                    continue;
                }
                continueRun = l.processInput(scanner,action);
            } while (continueRun);

        } catch (SQLException throwables) {
            System.out.println("cant connect to db");
        } catch (InputMismatchException e) {
            System.out.println("invalid action symbol");
        } catch (FileNotFoundException e) {
            System.out.println("properties file not found");
        } catch (ClassNotFoundException e) {
            System.out.println("driver class not found");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            if (l != null)
                l.closeConnection();
        }

    }


}
