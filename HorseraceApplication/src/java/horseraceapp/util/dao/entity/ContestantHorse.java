package horseraceapp.util.dao.entity;

import java.sql.Timestamp;

public class ContestantHorse {
    private Integer id;
    private String horseName;
    private Timestamp raceStartTime;
    private String racePlace;
    private Integer raceDistance;
    private Integer position;
    private Double coefficient;

    public ContestantHorse() {
    }

    public ContestantHorse(Integer id, String horseName, Timestamp raceStartTime, String racePlace, Integer raceDistance, Integer position, Double coefficient) {
        this.id = id;
        this.horseName = horseName;
        this.raceStartTime = raceStartTime;
        this.racePlace = racePlace;
        this.raceDistance = raceDistance;
        this.position = position;
        this.coefficient = coefficient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public Timestamp getRaceStartTime() {
        return raceStartTime;
    }

    public void setRaceStartTime(Timestamp raceStartTime) {
        this.raceStartTime = raceStartTime;
    }

    public String getRacePlace() {
        return racePlace;
    }

    public void setRacePlace(String racePlace) {
        this.racePlace = racePlace;
    }

    public Integer getRaceDistance() {
        return raceDistance;
    }

    public void setRaceDistance(Integer raceDistance) {
        this.raceDistance = raceDistance;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
}
