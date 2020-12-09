package horseraceapp.util.dao.entity;

import java.sql.Timestamp;

public class Race {
    private Integer id;
    private Timestamp startTime;
    private String place;
    private Integer distance;

    public Race() {
    }

    public Race(Integer id, Timestamp startTime, String place, Integer distance) {
        this.id = id;
        this.startTime = startTime;
        this.place = place;
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getPlace() {
        return place;
    }
    
    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
