package com.company.service.common;

import com.company.common.IEntity;
import com.company.service.domain.Service;

import java.util.List;

public interface IServiceDao extends IEntity<Service> {
    List<Service> getByTitle(String title);
    List<Service> getCostMoreThan(int cost);
    List<Service> getByDuration(int duration);

}
