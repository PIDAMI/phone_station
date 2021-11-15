package service.domain;

import common.IEntity;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public Long getId() {
        return id;
    }


    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Service other = (Service) obj;
        return Objects.equals(id, other.getId()) &&
                Objects.equals(title, other.getTitle()) &&
                Objects.equals(cost, other.getCost()) &&
                Objects.equals(duration, other.getDuration());

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
