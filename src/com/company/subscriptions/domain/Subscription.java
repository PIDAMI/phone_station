package com.company.subscriptions.domain;

import com.company.common.IEntity;

import java.sql.Date;
import java.util.Arrays;

public class Subscription implements IEntity {

    private Long id;
    private Long customerId;
    private Long serviceId;
    private Date beginDate;
    private Boolean isActive;

    public Subscription(Long id, Long customerId, Long serviceId,
                        Date beginDate, Boolean isActive) {
        this.id = id;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.beginDate = beginDate;
        this.isActive = isActive;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        String repres = Arrays.stream(getClass().getDeclaredFields())
                .map(s-> {
                    try {
                        return s.getName() + ":" + s.get(this) + ",";
                    } catch (IllegalAccessException e) {
                        return s.getName() + ":cant reach field,";
                    }
                })
                .reduce("[",(acc,s)->acc+s);
        builder.append(repres);
        builder.setCharAt(builder.length()-1,']');
        return builder.toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    @Override
    public Long getId() {
        return id;
    }
}
