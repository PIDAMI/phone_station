package com.company.service.common;

import com.company.common.ICrud;
import com.company.service.domain.Service;

import java.util.List;

public interface IServiceDao extends ICrud<Service> {
    List<Service> getByTitle(String title);
    List<Service> getCostMoreThan(int cost);
    List<Service> getByDuration(int duration);

}
