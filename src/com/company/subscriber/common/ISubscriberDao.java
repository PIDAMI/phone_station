package com.company.subscriber.common;

import com.company.common.ICrud;
import com.company.subscriber.domain.Subscriber;

import java.util.List;


public interface ISubscriberDao extends ICrud<Subscriber> {

    List<Subscriber> getOlderThan(int age);
    List<Subscriber> getByName(String name);
    List<Subscriber> getByPhone(String phone);
    List<Subscriber> getByCity(String city);
    int deleteByCity(String city);

}
