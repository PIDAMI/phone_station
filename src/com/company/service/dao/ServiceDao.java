package com.company.service.dao;

import com.company.service.common.IServiceDao;
import com.company.service.domain.Service;

import java.util.List;

public class ServiceDao implements IServiceDao {
    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public Boolean create(Service entity) {
        return null;
    }

    @Override
    public Service get(Long id) {
        return null;
    }

    @Override
    public List<Service> getAll() {
        return null;
    }

    @Override
    public Boolean update(Service entity) {
        return null;
    }

    @Override
    public Boolean delete(Service entity) {
        return null;
    }

    @Override
    public Boolean truncate() {
        return null;
    }

    @Override
    public List<Service> getByTitle(String title) {
        return null;
    }

    @Override
    public List<Service> getCostMoreThan(int cost) {
        return null;
    }

    @Override
    public List<Service> getByDuration(int duration) {
        return null;
    }
}
