package com.company.subscriber.dao;

import com.company.subscriber.common.ISubscriberDao;
import com.company.subscriber.domain.Subscriber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class SubscriberDao implements ISubscriberDao {


    private final String table = "subscriber";
    private final String createStatement =
            "INSERT INTO " + table + " (name,age,phone,city,street)" +
                    " VALUES (?,?,?,?,?)";
    private final String getStatement =
            "SELECT * FROM " + table + " WHERE id=?";
    private final String getAllStatement =
            "SELECT * FROM " + table;
    private final String updateStatement =
            "UPDATE " + table + " SET name=?, age=?, phone=?, city=?, street=? WHERE id=?";
    private final String deleteStatement =
            "DELETE * FROM" + table + " WHERE id=?";
    private final String truncateStatement = "DROP TABLE " + table;

    private final Connection connection;


    public SubscriberDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getTableName() {
        return table;
    }

    @Override
    public Boolean create(Subscriber entity) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(createStatement);
            statement.setString(1,entity.g)

        } catch (SQLException e) {
            System.out.println("failed to create new subscriber " + e.getMessage());
        }
        return null;
    }

    @Override
    public Subscriber get(Long id) {
        return null;
    }

    @Override
    public List<Subscriber> getAll() {
        return null;
    }

    @Override
    public Boolean update(Subscriber entity) {
        return null;
    }

    @Override
    public Boolean delete(Subscriber id) {
        return null;
    }

    @Override
    public Boolean truncate() {
        return null;
    }

    @Override
    public List<Subscriber> getOlderThan(int age) {
        return null;
    }

    @Override
    public List<Subscriber> getByName(String name) {
        return null;
    }

    @Override
    public List<Subscriber> getByPhone(String phone) {
        return null;
    }

    @Override
    public List<Subscriber> getByCity(String city) {
        return null;
    }

    @Override
    public int deleteByCity(String city) {
        return 0;
    }
}
