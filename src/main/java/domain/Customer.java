package domain;

import java.util.Arrays;
import java.util.Objects;

public class Customer implements IEntity {

    private Long id;
    private String name;
    private Integer age;
    private String phone;
    private String city;
    private String street;


    public Customer(Long id, String name, Integer age,
                    String phone, String city, String street) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.city = city;
        this.street = street;
    }


    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Customer other = (Customer) obj;
        return Objects.equals(id, other.getId()) &&
                Objects.equals(name, other.getName()) &&
                Objects.equals(age, other.getAge()) &&
                Objects.equals(phone, other.getPhone()) &&
                Objects.equals(city, other.getCity()) &&
                Objects.equals(street, other.getStreet());

    }


    public static void main(String[] args) {
        Customer customer = new Customer(1L,"zxc",1,"","","");
        System.out.println(customer);
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

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name == null ? "" : name;
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
