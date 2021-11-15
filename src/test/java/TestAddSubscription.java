import logic.Logic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TestAddSubscription {

    private static Logic logic;
    private static Scanner scanner;

    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";


    private final String DB_URL = "jdbc:mysql://localhost:3306/phone_service";

    //  Database credentials
    private final String USER = "phone_service";
    private final String PASS = "password";


    private static String getSql(final String resourceName) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Logic.class.getClassLoader()
                                                .getResourceAsStream(resourceName))))
                .lines()
                .collect(Collectors.joining("\n"));
    }

//    private void resetDBContent(){
//        try {
//
//            Class.forName(JDBC_DRIVER);
//            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
//                try (Statement statement = conn.createStatement()) {
//                    statement.execute(getSql("init-ddl.sql"));
//                }
//                try (Statement statement = conn.createStatement()) {
//                    statement.execute(getSql("init-dml.sql"));
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//
//    @BeforeAll
//    public void init() {
//        try{
//            logic = Logic.getInstance();
//            scanner = new Scanner();
//        } catch (ClassNotFoundException | SQLException e){
//            throw new RuntimeException(e);
//        }
//    }




//    @Test
//    public void addToExistingCustomer(){
//        Scanner scanner = new Scanner(new FileInputStream("x"));
//    }

}
