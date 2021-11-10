package com.company.subscriber.domain;

public class Subscriber {

    private final Long id;
    private String name;
    private Integer age;
    private String phone;
    private String city;
    private String street;

    public Subscriber(Long id, String name, Integer age,
                      String phone, String city, String street) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.city = city;
        this.street = street;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }
}
