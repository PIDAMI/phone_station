package dao;

import domain.Service;

import java.sql.SQLException;
import java.util.List;

public interface IServiceDao{
    List<Service> getByTitle(String title) throws SQLException;
    List<Service> getCostMoreThan(int cost) throws SQLException;
    List<Service> getByDuration(int duration) throws SQLException;

}
