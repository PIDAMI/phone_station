package com.company.subscriptions.domain;

import com.company.common.IEntity;

import java.sql.Date;

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
