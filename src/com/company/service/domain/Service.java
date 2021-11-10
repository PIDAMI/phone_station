package com.company.service.domain;

import com.company.common.IEntity;

public class Service implements IEntity {

    private Long id;
    private String title;
    private int cost;
    private int duration; //in days

    public Service(Long id, String title, int cost, int duration) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.duration = duration;
    }


    @Override
    public Long getId() {
        return id;
    }


    public void setTitle(String title) {this.title = title;}

    public void setCost(int cost) {this.cost = cost;}

    public void setDuration(int duration) {this.duration = duration;}

    public void setId(Long id ){ this.id = id;}


    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }

    public int getDuration() {
        return duration;
    }
}