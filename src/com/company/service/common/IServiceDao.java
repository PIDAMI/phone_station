package com.company.service.common;

import com.company.common.ICrud;
import com.company.service.domain.Service;

import java.sql.SQLException;
import java.util.List;

public interface IServiceDao extends ICrud<Service> {
    List<Service> getByTitle(String title) throws SQLException;
    List<Service> getCostMoreThan(int cost) throws SQLException;
    List<Service> getByDuration(int duration) throws SQLException;

}
