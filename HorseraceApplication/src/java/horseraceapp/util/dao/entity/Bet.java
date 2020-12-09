package horseraceapp.util.dao.entity;

import java.sql.Timestamp;

public class Bet {
    private Integer id;
    private User owner;
    private BetState state;
    private String horseName;
    private Double coefficient;
    private Integer amount;
    private String racePlace;
    private Timestamp raceStartTime;
    private Timestamp betPlaceTime;
    private Integer horsePosition;

    public Bet() {
    }

    public Bet(Integer id, User owner, BetState state, String horseName, Double coefficient, Integer amount, String racePlace, Timestamp raceStartTime, Timestamp betPlaceTime, Integer horsePosition) {
        this.id = id;
        this.owner = owner;
        this.state = state;
        this.horseName = horseName;
        this.coefficient = coefficient;
        this.amount = amount;
        this.racePlace = racePlace;
        this.raceStartTime = raceStartTime;
        this.betPlaceTime = betPlaceTime;
        this.horsePosition = horsePosition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public BetState getState() {
        return state;
    }

    public void setState(BetState state) {
        this.state = state;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRacePlace() {
        return racePlace;
    }

    public void setRacePlace(String racePlace) {
        this.racePlace = racePlace;
    }

    public Timestamp getRaceStartTime() {
        return raceStartTime;
    }

    public void setRaceStartTime(Timestamp raceStartTime) {
        this.raceStartTime = raceStartTime;
    }

    public Timestamp getBetPlaceTime() {
        return betPlaceTime;
    }

    public void setBetPlaceTime(Timestamp betPlaceTime) {
        this.betPlaceTime = betPlaceTime;
    }

    public Integer getHorsePosition() {
        return horsePosition;
    }

    public void setHorsePosition(Integer horsePosition) {
        this.horsePosition = horsePosition;
    }
}
